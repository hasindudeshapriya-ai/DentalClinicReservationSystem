document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch("api/session", { cache: "no-store" });
        if (!response.ok) return;
        const session = await response.json();
        if (!session.authenticated) return;

        const role = String(session.role || "").toUpperCase();

        document.querySelectorAll("a").forEach(link => {
            const href = (link.getAttribute("href") || "").toLowerCase();

            if (href.includes("user-management") && role !== "ADMIN") {
                link.style.display = "none";
            }

            if (role === "DENTIST") {
                if (href.includes("dentists.html") ||
                    href.includes("viewdentistsservlet") ||
                    href.includes("report.html") ||
                    href.includes("reportservlet")) {
                    link.style.display = "none";
                }

                if (href.includes("index.html") || href.includes("login.html")) {
                    link.setAttribute("href", "LogoutServlet");
                }
            }
        });

        
        if (role === "DENTIST") {
            if (location.pathname.toLowerCase().endsWith("/patients.html")) {
                const form = document.querySelector("form[action*='PatientServlet']");
                if (form) form.style.display = "none";
                document.querySelectorAll(".topbar h3").forEach(el => {
                    if (el.textContent.toLowerCase().includes("register")) {
                        el.textContent = "My Patients";
                    }
                });
            }

            if (location.pathname.toLowerCase().endsWith("/billing.html")) {
                document.querySelectorAll(".action-edit").forEach(el => el.style.display = "none");
                document.querySelectorAll("a[href*='CreateBillServlet'], a[href*='create-bill.html']")
                    .forEach(el => el.style.display = "none");
            }

            if (location.pathname.toLowerCase().endsWith("/view-bill.html")) {
                const editButton = document.getElementById("editBillButton");
                if (editButton) editButton.style.display = "none";
            }
        }

       
        const nameTargets = document.querySelectorAll(
            ".welcome-admin strong, #currentUserName, [data-current-user]"
        );
        nameTargets.forEach(el => el.textContent = session.fullName || session.username || role);

        const roleTargets = document.querySelectorAll(
            ".welcome-admin small, #currentUserRole, [data-current-role]"
        );
        roleTargets.forEach(el => el.textContent = role.charAt(0) + role.slice(1).toLowerCase());

    } catch (error) {
        console.warn("Role UI initialization failed:", error);
    }
});
