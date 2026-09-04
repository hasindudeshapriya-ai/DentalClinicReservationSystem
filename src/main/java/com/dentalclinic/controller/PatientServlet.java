/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.PatientDAO;
import com.dentalclinic.model.Patient;

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
@WebServlet("/PatientServlet")
public class PatientServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
       
       
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

       
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setGender(gender);
        patient.setDateOfBirth(dob);
        patient.setPhone(phone);
        patient.setEmail(email);
        patient.setAddress(address);
        

        PatientDAO dao = new PatientDAO();
        boolean status = dao.savePatient(patient);
        
        if (phone == null || !phone.matches("[0-9]{10}")) {

    response.setContentType("text/html");

    response.getWriter().println(
        "<script>" +
        "alert('Invalid phone number. Phone number must contain exactly 10 digits.');" +
        "history.back();" +
        "</script>"
    );

    return;
}
        if (status) {
            response.sendRedirect("patients.html?success=1");
        } else {
            response.sendRedirect("patients.html?error=1");
        }
    }
}