/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Appointment;

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
@WebServlet("/ViewAppointmentsServlet")
public class ViewAppointmentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        AppointmentDAO dao = new AppointmentDAO();
        List<Appointment> appointments;
        if (RoleAccess.isDentist(request)) {
            Integer dentistId = RoleAccess.dentistId(request);
            if (dentistId == null) { response.sendError(403, "Dentist profile is not linked."); return; }
            appointments = dao.getAppointmentsForDentist(dentistId);
        } else {
            appointments = dao.getAllAppointments();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<title>View Appointments</title>");
        out.println("<link rel='stylesheet' href='css/dashboard.css'>");
        out.println("<link rel='stylesheet' href='css/patients.css'>");
out.println("<link rel=\'stylesheet\' href=\'css/clinic-background.css\'>");
        out.println("</head>");

        out.println("<body>");
        
        out.println("<div class='sidebar'>");

        out.println("<h2>🦷 Dental Clinic</h2>");

        out.println("<a href='dashboard.html'>🏠 Dashboard</a>");
        out.println("<a href='patients.html'>👤 Patients</a>");
        out.println("<a href='LoadPatientsServlet'>📅 Appointments</a>");
        out.println("<a href='#'>🦷 Dentists</a>");
        out.println("<a href='#'>💳 Billing</a>");
        out.println("<a href='#'>📊 Reports</a>");
        out.println("<a href='#'>👥 User Management</a>");
        out.println("<a href='index.html'>🚪 Logout</a>");

        out.println("</div>");

        out.println("<div class='main'>");

        out.println("<div class='topbar'>");
        out.println("<h1>Appointment Management</h1>");
        out.println("<h3>View Appointments</h3>");
        out.println("</div>");
        
        out.println("<div class='top-buttons'>");

out.println("<a href='LoadPatientsServlet' class='view-btn'>");
out.println("← Back to Appointment");
out.println("</a>");

out.println("</div>");

    out.println("<div class='form-container'>");

out.println("<div class='table-container'>");

out.println("<table>");

out.println("<thead>");
out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Patient</th>");
        out.println("<th>Dentist</th>");
        out.println("<th>Date</th>");
        out.println("<th>Time</th>");
        out.println("<th>Treatment</th>");
        out.println("<th>Status</th>");
        out.println("<th>Action</th>");

        out.println("</tr>");
out.println("</thead>");

out.println("<tbody>");

        for (Appointment appointment : appointments) {

            out.println("<tr>");

            out.println("<td>" + appointment.getAppointmentId() + "</td>");
            out.println("<td>" + appointment.getPatientName() + "</td>");
            out.println("<td>" + appointment.getDentistName() + "</td>");
            out.println("<td>" + appointment.getAppointmentDate() + "</td>");
            out.println("<td>" + appointment.getAppointmentTime() + "</td>");
            out.println("<td>" + appointment.getTreatment() + "</td>");
            out.println("<td>" + appointment.getStatus() + "</td>");

            out.println("<td>");

            out.println("<a class='edit-btn' href='EditAppointmentServlet?id="
        + appointment.getAppointmentId() + "'>Edit</a>");
            

        out.println("<a class='delete-btn' href='DeleteAppointmentServlet?id="
        + appointment.getAppointmentId()
        + "' onclick=\"return confirm('Are you sure you want to delete this appointment?');\">Delete</a>");

            out.println("</td>");

            out.println("</tr>");
        }
    
        out.println("</tbody>");
        out.println("</table>");
        out.println("</div>");   
        out.println("</div>");   
        out.println("</div>");   

        out.println("<script src='js/role.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
