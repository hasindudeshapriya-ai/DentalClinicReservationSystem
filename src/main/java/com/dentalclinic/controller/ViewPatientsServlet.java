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
@WebServlet("/ViewPatientsServlet")
public class ViewPatientsServlet extends HttpServlet {

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

        out.println("<meta charset='UTF-8'>");

        out.println("<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>");

        out.println("<title>View Patients</title>");



        out.println("<link rel='stylesheet' "
                + "href='css/dashboard.css'>");

        out.println("<link rel='stylesheet' "
                + "href='css/patients.css'>");

        out.println("<link rel='stylesheet' "
                + "href='css/dentist.css'>");

        out.println("</head>");



        out.println("<body>");


        out.println("<div class='sidebar'>");

        out.println("<h2>🦷 Dental Clinic</h2>");

        out.println("<a href='dashboard.html'>"
                + "🏠 Dashboard</a>");

        out.println("<a href='patients.html' class='active'>"
                + "👤 Patients</a>");

        out.println("<a href='appointment.html'>"
                + "📅 Appointments</a>");

        out.println("<a href='dentists.html'>"
                + "🦷 Dentists</a>");

        out.println("<a href='billing.html'>"
                + "💳 Billing</a>");

        out.println("<a href='report.html'>"
                + "📊 Reports</a>");

        out.println("<a id='userManagementLink' "
                + "href='user-management.html'>"
                + "👥 User Management</a>");

        out.println("<a href='index.html'>"
                + "🚪 Logout</a>");

        out.println("</div>");


        out.println("<div class='main'>");



        out.println("<div class='topbar'>");

        out.println("<h1>Patient Management</h1>");

        if (RoleAccess.isDentist(request)) {

            out.println("<h3>My Patients</h3>");

        } else {

            out.println("<h3>View Patients</h3>");
        }

        out.println("</div>");



        out.println("<div class='top-buttons'>");

        out.println("<a href='patients.html' "
                + "class='view-btn'>");

        out.println("← Back to Patients");

        out.println("</a>");

        out.println("</div>");


        out.println("<div class='table-container'>");

        out.println("<table>");


        out.println("<thead>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>First Name</th>");
        out.println("<th>Last Name</th>");
        out.println("<th>Gender</th>");
        out.println("<th>Phone</th>");
        out.println("<th>Email</th>");
        out.println("<th>Action</th>");

        out.println("</tr>");

        out.println("</thead>");


        out.println("<tbody>");

        if (patients == null || patients.isEmpty()) {

            out.println("<tr>");

            out.println("<td colspan='7' "
                    + "style='text-align:center;'>");

            out.println("No patients found.");

            out.println("</td>");

            out.println("</tr>");

        } else {

            for (Patient p : patients) {

                out.println("<tr>");

                out.println("<td>"
                        + p.getPatientId()
                        + "</td>");

                out.println("<td>"
                        + p.getFirstName()
                        + "</td>");

                out.println("<td>"
                        + p.getLastName()
                        + "</td>");

                out.println("<td>"
                        + p.getGender()
                        + "</td>");

                out.println("<td>"
                        + p.getPhone()
                        + "</td>");

                out.println("<td>"
                        + p.getEmail()
                        + "</td>");


                out.println("<td>");

                if (!RoleAccess.isDentist(request)) {

                    out.println(
                            "<a class='edit-btn' "
                            + "href='EditPatientServlet?id="
                            + p.getPatientId()
                            + "'>Edit</a>"
                    );

                    out.println(" ");

                    out.println(
                            "<a class='delete-btn' "
                            + "href='DeletePatientServlet?id="
                            + p.getPatientId()
                            + "' "
                            + "onclick=\"return confirm("
                            + "'Are you sure you want to delete "
                            + "this patient?'"
                            + ");\">"
                            + "Delete"
                            + "</a>"
                    );

                } else {

                    out.println(
                            "<span class='view-only'>"
                            + "View Only"
                            + "</span>"
                    );
                }

                out.println("</td>");

                out.println("</tr>");
            }
        }

        out.println("</tbody>");

        out.println("</table>");

        out.println("</div>");



        out.println("</div>");


        out.println("<script "
                + "src='js/role.js'></script>");

        out.println("</body>");

        out.println("</html>");
    }
}
