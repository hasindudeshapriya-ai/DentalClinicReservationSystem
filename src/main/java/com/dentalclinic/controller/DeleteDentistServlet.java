/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.DentistDAO;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author hasin
 */
@WebServlet("/DeleteDentistServlet")
public class DeleteDentistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get dentist ID from URL
        int dentistId = Integer.parseInt(
                request.getParameter("id"));

        // Create DAO
        DentistDAO dao = new DentistDAO();

        // Delete dentist
        boolean deleted = dao.deleteDentist(dentistId);

        // Return to View Dentists page
        if (deleted) {

            response.sendRedirect("ViewDentistsServlet");

        } else {

            response.sendRedirect(
                    "ViewDentistsServlet?error=deleteFailed");
        }
    }
}