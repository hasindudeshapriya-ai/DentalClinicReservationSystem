/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.PatientDAO;
import com.dentalclinic.model.Patient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.dentalclinic.util.RoleAccess;

/**
 *
 * @author hasin
 */
@WebServlet("/LoadPatientsServlet")
public class LoadPatientsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        PatientDAO dao = new PatientDAO();
        List<Patient> patients;
        if (RoleAccess.isDentist(request)) {
            Integer dentistId = RoleAccess.dentistId(request);
            if (dentistId == null) { response.sendError(403, "Dentist profile is not linked."); return; }
            patients = dao.getPatientsForDentist(dentistId);
        } else {
            patients = dao.getAllPatients();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Book Appointment</title>");
        out.println("<link rel='stylesheet' href='css/dashboard.css'>");
        out.println("<link rel='stylesheet' href='css/appointment.css'>");
out.println("<link rel=\'stylesheet\' href=\'css/clinic-background.css\'>");
        out.println("</head>");

out.println("<body>");

out.println("<div class='sidebar'>");
out.println("<h2>🦷 Dental Clinic</h2>");
out.println("<a href='dashboard.html'>🏠 Dashboard</a>");
out.println("<a href='patients.html'>👤 Patients</a>");
out.println("<a href='LoadPatientsServlet' class='active'>📅 Appointments</a>");
out.println("<a href='dentists.html'>🦷 Dentists</a>");
out.println("<a href='#'>💳 Billing</a>");
out.println("<a href='#'>📊 Reports</a>");
out.println("<a href='#'>👥 User Management</a>");
out.println("<a href='index.html'>🚪 Logout</a>");
out.println("</div>");

out.println("<div class='main'>");

out.println("<div class='topbar'>");
out.println("<h1>Appointment Management</h1>");
out.println("<h3>Book New Appointment</h3>");
out.println("</div>");

out.println("<div class='top-buttons'>");

out.println("<a href='ViewAppointmentsServlet' class='view-btn'>");
out.println("📋 View All Appointments");
out.println("</a>");

out.println("</div>");

out.println("<div class='form-container'>");

out.println("<form action='AppointmentServlet' method='post'>");

out.println("<div class='row'>");

out.println("<div class='input-group'>");
out.println("<label>Patient</label>");
out.println("<select name='patientId' required>");
out.println("<option value=''>-- Select Patient --</option>");

for (Patient p : patients) {
    out.println("<option value='" + p.getPatientId() + "'>");
    out.println(p.getFirstName() + " " + p.getLastName());
    out.println("</option>");
}

out.println("</select>");
out.println("</div>");

out.println("<div class='input-group'>");
out.println("<label>Dentist</label>");
out.println("<select name='dentistId' required>");
if (RoleAccess.isDentist(request)) {
    Integer ownDentistId = RoleAccess.dentistId(request);
    out.println("<option value='" + ownDentistId + "' selected>Your appointments</option>");
} else {
    out.println("<option value='1'>Dr. Nimal Perera</option>");
    out.println("<option value='2'>Dr. Kasun Silva</option>");
    out.println("<option value='3'>Dr. Dinithi Fernando</option>");
}
out.println("</select>");
out.println("</div>");

out.println("</div>");

out.println("<div class='row'>");

out.println("<div class='input-group'>");
out.println("<label>Appointment Date</label>");
out.println("<input type='date' name='appointmentDate' required>");
out.println("</div>");

out.println("<div class='input-group'>");
out.println("<label>Appointment Time</label>");
out.println("<input type='time' name='appointmentTime' required>");
out.println("</div>");

out.println("</div>");

out.println("<div class='input-group'>");
out.println("<label>Treatment</label>");
out.println("<input type='text' name='treatment' placeholder='Enter Treatment' required>");
out.println("</div>");

out.println("<div class='input-group'>");
out.println("<label>Status</label>");
out.println("<select name='status'>");
out.println("<option value='Scheduled'>Scheduled</option>");
out.println("<option value='Completed'>Completed</option>");
out.println("<option value='Cancelled'>Cancelled</option>");
out.println("</select>");
out.println("</div>");

out.println("<div class='button-group'>");
out.println("<button type='submit' class='save-btn'>Book Appointment</button>");
out.println("<button type='reset' class='clear-btn'>Clear</button>");
out.println("</div>");

out.println("</form>");

out.println("</div>");
out.println("</div>");

out.println("<script src='js/role.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
