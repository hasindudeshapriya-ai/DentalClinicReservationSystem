/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.DentistDAO;
import com.dentalclinic.model.Dentist;

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
@WebServlet("/UpdateDentistServlet")
public class UpdateDentistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        
        int dentistId = Integer.parseInt(
                request.getParameter("dentistId"));

        String dentistName = request.getParameter("dentistName");

        String specialization = request.getParameter("specialization");

        String phone = request.getParameter("phone");

        String email = request.getParameter("email");


        
        Dentist dentist = new Dentist();

        dentist.setDentistId(dentistId);

        dentist.setDentistName(dentistName);

        dentist.setSpecialization(specialization);

        dentist.setPhone(phone);

        dentist.setEmail(email);


        
        DentistDAO dao = new DentistDAO();

        boolean updated = dao.updateDentist(dentist);
        
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

        if (updated) {

            response.sendRedirect("ViewDentistsServlet");

        } else {

            response.sendRedirect(
                    "ViewDentistsServlet?error=updateFailed");
        }
    }
}
 