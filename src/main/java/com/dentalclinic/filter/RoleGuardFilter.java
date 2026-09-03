package com.dentalclinic.filter;

import com.dentalclinic.util.RoleAccess;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class RoleGuardFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (isPublic(path) || isStatic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        String role = RoleAccess.role(req);

        if (session == null || role == null) {
            res.sendRedirect(req.getContextPath() + "/Login.html?error=session");
            return;
        }

        String p = path.toLowerCase();

        // Bill printing is Admin/Cashier only.
        if (p.contains("printbillservlet")) {
            if (!"ADMIN".equals(role) && !"CASHIER".equals(role)) {
                deny(res, "Only Admin and Cashier users can print bills.");
                return;
            }
        }

        // User Management is Admin only.
        if (p.contains("user-management") || p.equals("/api/users")
                || p.contains("usermanagementservlet")) {
            if (!"ADMIN".equals(role)) {
                deny(res, "Only Admin users can access User Management.");
                return;
            }
        }

        // Dentist management is not available to Dentists themselves.
        if ("DENTIST".equals(role) &&
                (p.contains("dentists.html") ||
                 p.contains("viewdentistsservlet") ||
                 p.contains("adddentistservlet") ||
                 p.contains("editdentistservlet") ||
                 p.contains("updatedentistservlet") ||
                 p.contains("deletedentistservlet") ||
                 p.contains("loaddentistsservlet") ||
                 p.contains("editdentistservlet") ||
                 p.contains("patientservlet") ||
                 p.contains("editpatientservlet") ||
                 p.contains("updatepatientservlet") ||
                 p.contains("deletepatientservlet") ||
                 p.contains("createbillservlet") ||
                 p.contains("updatebillservlet") ||
                 p.contains("deletebillservlet") ||
                 p.contains("report.html") ||
                 p.contains("reportservlet") ||
                 p.contains("/api/reports"))) {
            deny(res, "Dentists can access only their appointments, patients and bill/payment information.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        String p = path.toLowerCase();
        return p.isEmpty()
                || p.equals("/")
                || p.endsWith("/index.html")
                || p.endsWith("/login.html")
                || p.endsWith("/registration.html")
                || p.equals("/login")
                || p.equals("/loginservlet")
                || p.equals("/registeruserservlet")
                || p.equals("/api/session");
    }

    private boolean isStatic(String path) {
        String p = path.toLowerCase();
        return p.startsWith("/css/")
                || p.startsWith("/js/")
                || p.startsWith("/images/")
                || p.startsWith("/img/")
                || p.startsWith("/fonts/")
                || p.endsWith(".ico")
                || p.endsWith(".png")
                || p.endsWith(".jpg")
                || p.endsWith(".jpeg")
                || p.endsWith(".webp")
                || p.endsWith(".svg");
    }

    private void deny(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}
