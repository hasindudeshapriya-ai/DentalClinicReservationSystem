package com.dentalclinic.controller;

import com.dentalclinic.dao.PatientDAO;
import com.dentalclinic.model.Patient;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdatePatientServlet")
public class UpdatePatientServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

       
        String phone = request.getParameter("phone");

        if (phone == null || !phone.matches("[0-9]{10}")) {

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Invalid phone number. Phone number must contain exactly 10 digits.');" +
                "history.back();" +
                "</script>"
            );

            return;
        }

        Patient patient = new Patient();

        patient.setPatientId(
            Integer.parseInt(
                request.getParameter("patientId")
            )
        );

        patient.setFirstName(
            request.getParameter("firstName")
        );

        patient.setLastName(
            request.getParameter("lastName")
        );

        patient.setGender(
            request.getParameter("gender")
        );

        patient.setDateOfBirth(
            request.getParameter("dob")
        );

        patient.setPhone(phone);

        patient.setEmail(
            request.getParameter("email")
        );

        patient.setAddress(
            request.getParameter("address")
        );

        PatientDAO dao = new PatientDAO();

        boolean status = dao.updatePatient(patient);

        if (status) {

            response.sendRedirect(
                "ViewPatientsServlet"
            );

        } else {

            response.setContentType("text/html");

            response.getWriter().println(
                "<script>" +
                "alert('Patient update failed!');" +
                "history.back();" +
                "</script>"
            );
        }
    }
}