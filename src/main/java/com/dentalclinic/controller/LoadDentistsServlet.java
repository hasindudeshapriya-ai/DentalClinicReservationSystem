/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author hasin
 */
@WebServlet("/LoadDentistsServlet")
public class LoadDentistsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String sql = "SELECT dentist_id, dentist_name "
                   + "FROM dentists "
                   + "ORDER BY dentist_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            out.print("[");

            boolean first = true;

            while (resultSet.next()) {

                if (!first) {
                    out.print(",");
                }

                int dentistId = resultSet.getInt("dentist_id");
                String dentistName = resultSet.getString("dentist_name");

                // Basic JSON escaping
                dentistName = dentistName
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"");

                out.print("{");
                out.print("\"dentistId\":" + dentistId + ",");
                out.print("\"dentistName\":\"" + dentistName + "\"");
                out.print("}");

                first = false;
            }

            out.print("]");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print("{\"error\":\"Unable to load dentists\"}");
        }
    }
}