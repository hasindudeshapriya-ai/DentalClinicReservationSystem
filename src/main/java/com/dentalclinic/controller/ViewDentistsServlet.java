/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.DentistDAO;
import com.dentalclinic.model.Dentist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author hasin
 */
@WebServlet("/ViewDentistsServlet")
public class ViewDentistsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        DentistDAO dao = new DentistDAO();

        List<Dentist> dentists = dao.getAllDentists();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>View Dentists</title>");

        out.println("<link rel='stylesheet' href='css/dashboard.css'>");
        out.println("<link rel='stylesheet' href='css/patients.css'>");
        out.println("<link rel='stylesheet' href='css/dentist.css'>");
out.println("<link rel=\'stylesheet\' href=\'css/clinic-background.css\'>");

        out.println("</head>");

        out.println("<body>");

        

        out.println("<div class='sidebar'>");

        out.println("<h2>🦷 Dental Clinic</h2>");

        out.println("<a href='dashboard.html'>🏠 Dashboard</a>");

        out.println("<a href='patients.html'>👤 Patients</a>");

        out.println("<a href='appointment.html'>📅 Appointments</a>");

        out.println("<a href='dentists.html' class='active'>🦷 Dentists</a>");

        out.println("<a href='#'>💳 Billing</a>");

        out.println("<a href='#'>📊 Reports</a>");

        out.println("<a href='#'>👥 User Management</a>");

        out.println("<a href='index.html'>🚪 Logout</a>");

        out.println("</div>");


        

        out.println("<div class='main'>");


        
        out.println("<div class='topbar'>");

        out.println("<h1>Dentist Management</h1>");

        out.println("<h3>View Dentists</h3>");

        out.println("</div>");


        
        out.println("<div class='top-buttons'>");

        out.println("<a href='dentists.html' class='view-btn'>");

        out.println("← Back to Dentist");

        out.println("</a>");

        out.println("</div>");


        
        out.println("<div class='table-container'>");

        out.println("<table>");


        
        out.println("<thead>");

        out.println("<tr>");

        out.println("<th>ID</th>");
        out.println("<th>Dentist Name</th>");
        out.println("<th>Specialization</th>");
        out.println("<th>Phone</th>");
        out.println("<th>Email</th>");
        out.println("<th>Action</th>");

        out.println("</tr>");

        out.println("</thead>");


        
        out.println("<tbody>");

        for (Dentist dentist : dentists) {

            out.println("<tr>");

            out.println("<td>"
                    + dentist.getDentistId()
                    + "</td>");

            out.println("<td>"
                    + dentist.getDentistName()
                    + "</td>");

            out.println("<td>"
                    + dentist.getSpecialization()
                    + "</td>");

            out.println("<td>"
                    + dentist.getPhone()
                    + "</td>");

            out.println("<td>"
                    + dentist.getEmail()
                    + "</td>");


           
            out.println("<td>");

            out.println("<a class='edit-btn' "
                    + "href='EditDentistServlet?id="
                    + dentist.getDentistId()
                    + "'>Edit</a>");

            out.println(" ");

            out.println("<a class='delete-btn' "
                    + "href='DeleteDentistServlet?id="
                    + dentist.getDentistId()
                    + "' "
                    + "onclick=\"return confirm('Are you sure you want to delete this dentist?');\">"
                    + "Delete</a>");

            out.println("</td>");

            out.println("</tr>");
        }

        out.println("</tbody>");

        out.println("</table>");

        out.println("</div>");


        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}
