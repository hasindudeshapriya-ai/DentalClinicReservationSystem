/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.util.DBConnection;
import com.dentalclinic.util.RoleAccess;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author hasin
 */
@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String role = RoleAccess.role(request);

        if (role == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Not logged in\"}"
            );

            return;
        }

        role = role.toUpperCase();

        Integer dentistId = null;

        if ("DENTIST".equals(role)) {

            Object id =
                    request.getSession().getAttribute("dentistId");

            if (id == null) {

                response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN
                );

                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Dentist profile not found\"}"
                );

                return;
            }

            dentistId = Integer.parseInt(id.toString());
        }

        try (Connection con = DBConnection.getConnection()) {

            Map<String, Object> data =
                    new LinkedHashMap<>();


            String patientSql;

            if ("DENTIST".equals(role)) {

                patientSql =
                        "SELECT COUNT(DISTINCT patient_id) " +
                        "FROM appointments " +
                        "WHERE dentist_id = ?";

            } else {

                patientSql =
                        "SELECT COUNT(*) FROM patients";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(patientSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "totalPatients",
                            rs.getInt(1)
                    );
                }
            }


            String appointmentSql;

            if ("DENTIST".equals(role)) {

                appointmentSql =
                        "SELECT COUNT(*) " +
                        "FROM appointments " +
                        "WHERE dentist_id = ?";

            } else {

                appointmentSql =
                        "SELECT COUNT(*) FROM appointments";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(appointmentSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "totalAppointments",
                            rs.getInt(1)
                    );
                }
            }



            if ("DENTIST".equals(role)) {

                data.put("totalDentists", 1);

            } else {

                String sql =
                        "SELECT COUNT(*) FROM dentists";

                try (PreparedStatement ps =
                             con.prepareStatement(sql);
                     ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "totalDentists",
                            rs.getInt(1)
                    );
                }
            }



            String revenueSql;

            if ("DENTIST".equals(role)) {

                revenueSql =
                        "SELECT COALESCE(SUM(b.amount_paid),0) " +
                        "FROM bills b " +
                        "WHERE b.dentist_id = ?";

            } else {

                revenueSql =
                        "SELECT COALESCE(SUM(amount_paid),0) " +
                        "FROM bills";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(revenueSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "totalRevenue",
                            rs.getDouble(1)
                    );
                }
            }



            String todaySql =
                    "SELECT " +
                    "SUM(CASE WHEN LOWER(status) IN " +
                    "('scheduled','pending') THEN 1 ELSE 0 END), " +

                    "SUM(CASE WHEN LOWER(status) = " +
                    "'completed' THEN 1 ELSE 0 END), " +

                    "SUM(CASE WHEN LOWER(status) = " +
                    "'cancelled' THEN 1 ELSE 0 END), " +

                    "COUNT(*) " +

                    "FROM appointments " +

                    "WHERE appointment_date = CURDATE()";

            if ("DENTIST".equals(role)) {

                todaySql +=
                        " AND dentist_id = ?";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(todaySql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "todayScheduled",
                            rs.getInt(1)
                    );

                    data.put(
                            "todayCompleted",
                            rs.getInt(2)
                    );

                    data.put(
                            "todayCancelled",
                            rs.getInt(3)
                    );

                    data.put(
                            "todayTotal",
                            rs.getInt(4)
                    );
                }
            }



            String pendingSql =
                    "SELECT COUNT(*) " +
                    "FROM appointments " +
                    "WHERE LOWER(status) IN " +
                    "('scheduled','pending')";

            if ("DENTIST".equals(role)) {

                pendingSql +=
                        " AND dentist_id = ?";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(pendingSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "pendingAppointments",
                            rs.getInt(1)
                    );
                }
            }



            String completedSql =
                    "SELECT COUNT(*) " +
                    "FROM appointments " +
                    "WHERE LOWER(status) = 'completed'";

            if ("DENTIST".equals(role)) {

                completedSql +=
                        " AND dentist_id = ?";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(completedSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "completedAppointments",
                            rs.getInt(1)
                    );
                }
            }



            String outstandingSql =
                    "SELECT COUNT(*) " +
                    "FROM bills " +
                    "WHERE balance > 0";

            if ("DENTIST".equals(role)) {

                outstandingSql +=
                        " AND dentist_id = ?";
            }

            try (PreparedStatement ps =
                         con.prepareStatement(outstandingSql)) {

                if ("DENTIST".equals(role)) {
                    ps.setInt(1, dentistId);
                }

                try (ResultSet rs =
                             ps.executeQuery()) {

                    rs.next();

                    data.put(
                            "outstandingBills",
                            rs.getInt(1)
                    );
                }
            }



            data.put(
                    "role",
                    role
            );

            data.put(
                    "success",
                    true
            );



            StringBuilder json =
                    new StringBuilder("{");

            boolean first = true;

            for (Map.Entry<String, Object> entry
                    : data.entrySet()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                json.append("\"")
                        .append(entry.getKey())
                        .append("\":");

                Object value =
                        entry.getValue();

                if (value instanceof Number) {

                    json.append(value);

                } else if (value instanceof Boolean) {

                    json.append(value);

                } else {

                    json.append("\"")
                            .append(
                                    String.valueOf(value)
                                            .replace("\"", "\\\"")
                            )
                            .append("\"");
                }
            }

            json.append("}");

            response.getWriter()
                    .write(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"success\":false," +
                    "\"message\":\"Unable to load dashboard data\"}"
            );
        }
    }
}
