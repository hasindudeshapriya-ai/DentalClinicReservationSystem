/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.PatientDAO;
import com.dentalclinic.model.Patient;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author hasin
 */
@WebServlet("/EditPatientServlet")
public class EditPatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));

        PatientDAO dao = new PatientDAO();

        Patient patient = dao.getPatientById(id);

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Edit Patient</title>");

        out.println("<style>");
        out.println("body{font-family:Arial;background:#f4f6f9;padding:30px;}");
        out.println(".container{width:600px;margin:auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 10px #ccc;}");
        out.println("input,select,textarea{width:100%;padding:10px;margin-top:8px;margin-bottom:15px;}");
        out.println("button{background:#1565c0;color:white;padding:10px 20px;border:none;border-radius:5px;cursor:pointer;}");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<div class='container'>");
        out.println("<h2>Edit Patient</h2>");

        out.println("<form action='UpdatePatientServlet' method='post'>");

        out.println("<input type='hidden' name='patientId' value='" + patient.getPatientId() + "'>");

        out.println("First Name");
        out.println("<input type='text' name='firstName' value='" + patient.getFirstName() + "' required>");

        out.println("Last Name");
        out.println("<input type='text' name='lastName' value='" + patient.getLastName() + "' required>");

        out.println("Gender");
        out.println("<select name='gender'>");

        if(patient.getGender().equals("Male")){
            out.println("<option selected>Male</option>");
            out.println("<option>Female</option>");
        } else {
            out.println("<option>Male</option>");
            out.println("<option selected>Female</option>");
        }

        out.println("</select>");

        out.println("Date of Birth");
        out.println("<input type='date' name='dob' value='" + patient.getDateOfBirth() + "'>");

        out.println("Phone");
        out.println("<input type='text' name='phone' value='" + patient.getPhone() + "'>");

        out.println("Email");
        out.println("<input type='email' name='email' value='" + patient.getEmail() + "'>");

        out.println("Address");
        out.println("<textarea name='address'>" + patient.getAddress() + "</textarea>");

        out.println("<button type='submit'>Update Patient</button>");

        out.println("</form>");

        out.println("</div>");
        out.println("<script src='js/role.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
