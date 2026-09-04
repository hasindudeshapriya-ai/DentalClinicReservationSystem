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
@WebServlet("/AddDentistServlet")
public class AddDentistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        
        String dentistName = request.getParameter("dentistName");
        String specialization = request.getParameter("specialization");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        
        Dentist dentist = new Dentist();

        dentist.setDentistName(dentistName);
        dentist.setSpecialization(specialization);
        dentist.setPhone(phone);
        dentist.setEmail(email);

       
        DentistDAO dao = new DentistDAO();

        boolean saved = dao.saveDentist(dentist);

       
        if (saved) {

            response.sendRedirect("dentists.html");

        } else {

            response.sendRedirect("dentists.html?error=failed");
        }
    }
}