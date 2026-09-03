let allUsers = [];
let editMode = false;

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("addUserButton")?.addEventListener("click", openAddUserModal);
    document.getElementById("closeModalBtn")?.addEventListener("click", closeUserModal);
    document.getElementById("cancelBtn")?.addEventListener("click", closeUserModal);
    document.getElementById("userForm")?.addEventListener("submit", saveUser);
    document.getElementById("searchUser")?.addEventListener("input", filterUsers);
    document.getElementById("roleFilter")?.addEventListener("change", filterUsers);
    document.getElementById("statusFilter")?.addEventListener("change", filterUsers);

    loadUsers();
});

async function loadUsers() {
    const tableBody = document.getElementById("usersTableBody");
    if (!tableBody) return;

    tableBody.innerHTML = '<tr><td colspan="8" class="loading-message">Loading users...</td></tr>';

    try {
        const response = await fetch("api/users", {
            method: "GET",
            headers: { "Accept": "application/json" }
        });
        const data = await response.json();

        if (!response.ok || !data.success) {
            throw new Error(data.message || "Failed to load users");
        }

        allUsers = Array.isArray(data.users) ? data.users : [];
        displayUsers(allUsers);
    } catch (error) {
        console.error("Load users error:", error);
        tableBody.innerHTML = '<tr><td colspan="8" class="error">Failed to load users.</td></tr>';
    }
}

function displayUsers(users) {
    const tableBody = document.getElementById("usersTableBody");
    const userCount = document.getElementById("userCount");
    if (!tableBody) return;

    if (userCount) {
        userCount.textContent = `${users.length} User${users.length === 1 ? "" : "s"}`;
    }

    if (!users.length) {
        tableBody.innerHTML = '<tr><td colspan="8" class="no-data">No users found.</td></tr>';
        return;
    }

    tableBody.innerHTML = users.map(user => {
        const role = String(user.role || "N/A").toUpperCase();
        const status = String(user.status || "ACTIVE").toUpperCase();
        return `
            <tr>
                <td>${escapeHTML(user.userId)}</td>
                <td><strong>${escapeHTML(user.username || "")}</strong></td>
                <td>${escapeHTML(user.fullName || "N/A")}</td>
                <td>${escapeHTML(user.email || "N/A")}</td>
                <td><span class="role-badge ${role.toLowerCase()}">${escapeHTML(role)}</span></td>
                <td><span class="status-badge ${status.toLowerCase()}">${escapeHTML(status)}</span></td>
                <td>${escapeHTML(user.createdAt || "N/A")}</td>
                <td>
                    <div class="action-buttons">
                        <button type="button" class="btn-edit" onclick="editUser(${Number(user.userId)})">Edit</button>
                        <button type="button" class="btn-delete" onclick="deleteUser(${Number(user.userId)})">Delete</button>
                    </div>
                </td>
            </tr>`;
    }).join("");
}

function openAddUserModal() {
    editMode = false;
    const form = document.getElementById("userForm");
    if (form) form.reset();

    document.getElementById("modalTitle").textContent = "Add User";
    document.getElementById("userId").value = "";
    document.getElementById("password").required = true;
    document.getElementById("passwordHelp").textContent = "Required when creating a user.";
    document.getElementById("status").value = "ACTIVE";
    clearFormMessage();
    document.getElementById("userModal").classList.add("show");
}

function editUser(userId) {
    const user = allUsers.find(u => Number(u.userId) === Number(userId));
    if (!user) {
        alert("User not found.");
        return;
    }

    editMode = true;
    document.getElementById("modalTitle").textContent = "Edit User";
    document.getElementById("userId").value = user.userId;
    document.getElementById("username").value = user.username || "";
    document.getElementById("fullName").value = user.fullName || "";
    document.getElementById("email").value = user.email || "";
    document.getElementById("role").value = String(user.role || "").toUpperCase();
    document.getElementById("status").value = String(user.status || "ACTIVE").toUpperCase();
    document.getElementById("password").value = "";
    document.getElementById("password").required = false;
    document.getElementById("passwordHelp").textContent = "Leave blank to keep the current password.";
    clearFormMessage();
    document.getElementById("userModal").classList.add("show");
}

function closeUserModal() {
    document.getElementById("userModal")?.classList.remove("show");
    clearFormMessage();
}

async function saveUser(event) {
    event.preventDefault();

    const userId = document.getElementById("userId").value;
    const username = document.getElementById("username").value.trim();
    const fullName = document.getElementById("fullName").value.trim();
    const email = document.getElementById("email").value.trim();
    const role = document.getElementById("role").value;
    const password = document.getElementById("password").value;
    const status = document.getElementById("status").value;

    if (!username || !fullName || !role) {
        showFormMessage("Please fill all required fields.", "error");
        return;
    }
    if (!editMode && !password) {
        showFormMessage("Password is required for a new user.", "error");
        return;
    }

    const userData = { username, fullName, email, role, status };
    if (password) userData.password = password;

    const url = editMode ? `api/users?id=${encodeURIComponent(userId)}` : "api/users";
    const method = editMode ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json", "Accept": "application/json" },
            body: JSON.stringify(userData)
        });
        const data = await response.json();

        if (!response.ok || !data.success) {
            throw new Error(data.message || "Unable to save user");
        }

        alert(editMode ? "User updated successfully." : "User added successfully.");
        closeUserModal();
        await loadUsers();
    } catch (error) {
        console.error("Save user error:", error);
        showFormMessage(error.message || "Unable to save user.", "error");
    }
}

async function deleteUser(userId) {
    const user = allUsers.find(u => Number(u.userId) === Number(userId));
    if (!user) return alert("User not found.");
    if (Number(userId) === 1) return alert("The main administrator account cannot be deleted.");
    if (!confirm(`Are you sure you want to delete "${user.username}"?`)) return;

    try {
        const response = await fetch(`api/users?id=${encodeURIComponent(userId)}`, { method: "DELETE" });
        const data = await response.json();
        if (!response.ok || !data.success) throw new Error(data.message || "Unable to delete user");
        alert("User deleted successfully.");
        await loadUsers();
    } catch (error) {
        console.error("Delete user error:", error);
        alert(error.message || "Unable to delete user.");
    }
}

function filterUsers() {
    const search = (document.getElementById("searchUser")?.value || "").toLowerCase().trim();
    const role = (document.getElementById("roleFilter")?.value || "all").toUpperCase();
    const status = (document.getElementById("statusFilter")?.value || "all").toUpperCase();

    const filtered = allUsers.filter(user => {
        const username = String(user.username || "").toLowerCase();
        const fullName = String(user.fullName || "").toLowerCase();
        const email = String(user.email || "").toLowerCase();
        const userRole = String(user.role || "").toUpperCase();
        const userStatus = String(user.status || "ACTIVE").toUpperCase();

        return (!search || username.includes(search) || fullName.includes(search) || email.includes(search))
            && (role === "ALL" || userRole === role)
            && (status === "ALL" || userStatus === status);
    });

    displayUsers(filtered);
}

function showFormMessage(message, type) {
    const box = document.getElementById("formMessage");
    if (!box) return;
    box.textContent = message;
    box.className = `form-message ${type || ""}`;
}

function clearFormMessage() {
    const box = document.getElementById("formMessage");
    if (!box) return;
    box.textContent = "";
    box.className = "form-message";
}

function escapeHTML(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


window.openAddUserModal = openAddUserModal;
window.editUser = editUser;
window.closeUserModal = closeUserModal;
window.deleteUser = deleteUser;
