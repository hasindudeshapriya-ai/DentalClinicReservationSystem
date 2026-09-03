package com.dentalclinic.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public final class RoleAccess {
    private RoleAccess() {}

    public static String role(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("role");
        return value == null ? null : value.toString().toUpperCase();
    }

    public static Integer dentistId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("dentistId");
        if (value instanceof Integer) return (Integer) value;
        if (value == null) return null;
        try { return Integer.valueOf(value.toString()); }
        catch (NumberFormatException e) { return null; }
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return "ADMIN".equals(role(request));
    }

    public static boolean isCashier(HttpServletRequest request) {
        return "CASHIER".equals(role(request));
    }

    public static boolean isDentist(HttpServletRequest request) {
        return "DENTIST".equals(role(request));
    }

    public static boolean allowed(HttpServletRequest request, String... roles) {
        String role = role(request);
        if (role == null) return false;
        for (String allowed : roles) {
            if (allowed.equalsIgnoreCase(role)) return true;
        }
        return false;
    }

    public static boolean require(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String... roles) throws IOException {
        if (allowed(request, roles)) return true;
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Access denied. Your role does not have permission for this function.");
        return false;
    }
}
