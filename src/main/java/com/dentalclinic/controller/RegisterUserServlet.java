package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUserServlet"})
public class RegisterUserServlet extends HttpServlet {

    private UserDAO userDAO;

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z.\\s]+$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_.-]{3,30}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    private static final Pattern SPECIALIZATION_PATTERN =
            Pattern.compile("^[A-Za-z0-9&.,'()\\-\\s]+$");

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = trim(request.getParameter("fullName"));
        String username = trim(request.getParameter("username"));
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        String role = trim(request.getParameter("role")).toUpperCase();
        String specialization = trim(request.getParameter("specialization"));
        String phone = trim(request.getParameter("phone"));

        // =========================
        // EMPTY FIELD VALIDATION
        // =========================
        if (isBlank(fullName) || isBlank(username) || isBlank(email)
                || isBlank(password) || isBlank(role)) {
            redirectError(response, "Please fill all required fields.");
            return;
        }

        // =========================
        // NAME VALIDATION
        // =========================
        if (!NAME_PATTERN.matcher(fullName).matches()) {
            redirectError(response, "Full name can contain letters, spaces and dots only.");
            return;
        }

        // =========================
        // USERNAME VALIDATION
        // =========================
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            redirectError(response,
                    "Username must be 3-30 characters and may contain letters, numbers, dot, underscore or hyphen.");
            return;
        }

        // =========================
        // EMAIL VALIDATION
        // =========================
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            redirectError(response, "Please enter a valid email address.");
            return;
        }

        // =========================
        // PASSWORD VALIDATION
        // =========================
        if (password.length() < 6) {
            redirectError(response, "Password must contain at least 6 characters.");
            return;
        }

        // =========================
        // PUBLIC ROLE SECURITY
        // =========================
        if (!"DENTIST".equals(role) && !"CASHIER".equals(role)) {
            redirectError(response,
                    "Only Dentist or Cashier registration is allowed here.");
            return;
        }

        // =========================
        // DUPLICATE USERNAME
        // =========================
        if (userDAO.usernameExists(username)) {
            redirectError(response,
                    "Username already exists. Please choose another username.");
            return;
        }

        // =========================
        // PHONE VALIDATION
        // =========================
        // Phone is required for EVERY public registration.
        // Validate on the server so invalid values cannot bypass JavaScript.
        if (isBlank(phone)) {
            redirectError(response, "Phone number is required.");
            return;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            redirectError(response,
                    "Phone number must contain exactly 10 digits (numbers only).");
            return;
        }

        // =========================
        // DENTIST VALIDATION
        // =========================
        if ("DENTIST".equals(role)) {

            if (isBlank(specialization)) {
                redirectError(response,
                        "Dentist registration requires a specialization.");
                return;
            }

            if (!SPECIALIZATION_PATTERN.matcher(specialization).matches()) {
                redirectError(response, "Please enter a valid specialization.");
                return;
            }
        } else {
            // Cashiers do not create a dentist profile.
            specialization = "";
        }

        try {

            boolean success = userDAO.registerUserAndProfile(
                    username,
                    password,
                    fullName,
                    email,
                    role,
                    specialization,
                    phone
            );

            if (success) {
                redirectSuccess(response,
                        "Registration successful. You can now login.");
            } else {
                redirectError(response,
                        "Registration failed. Please try again.");
            }

        } catch (Exception e) {

            e.printStackTrace();

            redirectError(response,
                    "Registration failed because of a database error.");
        }
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void redirectError(HttpServletResponse response,
                               String message) throws IOException {
        response.sendRedirect(
                "registration.html?error="
                + encode(message)
        );
    }

    private void redirectSuccess(HttpServletResponse response,
                                 String message) throws IOException {
        response.sendRedirect(
                "registration.html?success="
                + encode(message)
        );
    }

    private String encode(String value) {
        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}
