
document.addEventListener("DOMContentLoaded", async function () {
    const sidebar = document.querySelector(".sidebar");
    if (!sidebar) return;

    const path = window.location.pathname.toLowerCase();

    let session = null;
    try {
        const response = await fetch("api/session", { cache: "no-store" });
        if (response.ok) session = await response.json();
    } catch (error) {
        console.warn("Unable to load session for sidebar.", error);
    }

    const role = String(session?.role || "").toUpperCase();

    const links = [
        ["dashboard.html", "🏠 Dashboard", ["dashboard.html"]],
        ["patients.html", "👤 Patients", ["patients.html"]],
        ["appointment.html", "📅 Appointments", ["appointment.html", "viewappointmentsservlet", "editappointmentservlet"]],
        ["dentists.html", "🦷 Dentists", ["dentists.html", "viewdentistsservlet"]],
        ["billing.html", "💳 Billing", ["billing.html", "billingservlet", "create-bill.html", "edit-bill.html", "view-bill.html", "createbillservlet", "updatebillservlet", "viewbillservlet"]],
        ["report.html", "📊 Reports", ["report.html", "reportservlet"]],
        ["user-management.html", "👥 User Management", ["user-management.html", "usermanagementservlet", "api/users"]]
    ];

    sidebar.innerHTML = "";

    const title = document.createElement("h2");
    title.textContent = "🦷 Dental Clinic";
    sidebar.appendChild(title);

    const nav = document.createElement("nav");
    nav.className = "sidebar-nav";

    links.forEach(([href, label, matches]) => {
        
        if (role !== "ADMIN" && href === "user-management.html") return;
        if (role === "DENTIST" && (href === "dentists.html" || href === "report.html")) return;

        const a = document.createElement("a");
        a.href = href;
        a.textContent = label;

        if (matches.some(m => path.includes(m))) {
            a.classList.add("active");
        }

        nav.appendChild(a);
    });

    const logout = document.createElement("a");
    logout.href = "LogoutServlet";
    logout.textContent = "🚪 Logout";
    nav.appendChild(logout);

    sidebar.appendChild(nav);
});
