/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.PatientDAO;

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
@WebServlet("/DeletePatientServlet")
public class DeletePatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int patientId = Integer.parseInt(request.getParameter("id"));

        PatientDAO dao = new PatientDAO();

        boolean status = dao.deletePatient(patientId);

        if (status) {
            response.sendRedirect("ViewPatientsServlet");
        } else {
            response.getWriter().println("Failed to delete patient.");
        }
    }
}
