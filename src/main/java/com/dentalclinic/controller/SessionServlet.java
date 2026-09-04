package com.dentalclinic.controller;

import com.dentalclinic.util.RoleAccess;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/session")
public class SessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String role = RoleAccess.role(request);
        if (role == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{\"authenticated\":false}");
            return;
        }
        Object userId = request.getSession().getAttribute("userId");
        Object username = request.getSession().getAttribute("username");
        Object fullName = request.getSession().getAttribute("fullName");
        Object dentistId = request.getSession().getAttribute("dentistId");
        response.getWriter().print("{"
                + "\"authenticated\":true,"
                + "\"userId\":" + (userId == null ? "null" : userId) + ","
                + "\"username\":\"" + esc(username) + "\","
                + "\"fullName\":\"" + esc(fullName) + "\","
                + "\"role\":\"" + esc(role) + "\","
                + "\"dentistId\":" + (dentistId == null ? "null" : dentistId)
                + "}");
    }
    private String esc(Object value) {
        if (value == null) return "";
        return value.toString().replace("\\","\\\\").replace("\"","\\\"");
    }
}
