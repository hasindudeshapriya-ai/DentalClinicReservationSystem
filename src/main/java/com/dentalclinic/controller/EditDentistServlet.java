/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.DentistDAO;
import com.dentalclinic.model.Dentist;

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
@WebServlet("/EditDentistServlet")
public class EditDentistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

       
        int dentistId = Integer.parseInt(
                request.getParameter("id"));

      
        DentistDAO dao = new DentistDAO();

       
        Dentist dentist = dao.getDentistById(dentistId);

        
        if (dentist == null) {

            out.println("<h2>Dentist not found.</h2>");
            return;
        }

      
        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Edit Dentist</title>");

        out.println("<link rel='stylesheet' href='css/dashboard.css'>");
        out.println("<link rel='stylesheet' href='css/patients.css'>");
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

out.println("<h3>Edit Dentist</h3>");

out.println("</div>");


out.println("<div class='form-container'>");

out.println("<form action='UpdateDentistServlet' method='post'>");

out.println("<input type='hidden' "
        + "name='dentistId' "
        + "value='" + dentist.getDentistId() + "'>");

out.println("<div class='row'>");


out.println("<div class='input-group'>");

out.println("<label>Dentist Name</label>");

out.println("<input type='Name' "
        + "name='dentistName' "
        + "value='" + dentist.getDentistName() + "' "
        + "required>");

out.println("<small id='dentistPhoneError' "
        + "class='field-error'></small>");

out.println("</div>");



out.println("<div class='input-group'>");

out.println("<label>Specialization</label>");

out.println("<input type='text' "
        + "name='specialization' "
        + "value='" + dentist.getSpecialization() + "' "
        + "required>");

out.println("</div>");

out.println("</div>");


out.println("<div class='row'>");



out.println("<div class='input-group'>");

out.println("<label>Phone</label>");

out.println("<input type='text' "
        + "name='phone' "
        + "value='" + dentist.getPhone() + "' "
        + "required>");

out.println("</div>");



out.println("<div class='input-group'>");

out.println("<label>Email</label>");

out.println("<input type='email' "
        + "name='email' "
        + "value='" + dentist.getEmail() + "' "
        + "required>");

out.println("</div>");

out.println("</div>");


out.println("<div class='button-group'>");

out.println("<button type='submit' class='save-btn'>"
        + "Update Dentist"
        + "</button>");

out.println("<a href='ViewDentistsServlet' class='clear-btn'>"
        + "Cancel"
        + "</a>");

out.println("</div>");

out.println("</form>");


out.println("</div>");

out.println("</div>");

out.println("<script>");

out.println("document.querySelector('form').addEventListener('submit', function(event) {");

out.println("const phone = document.getElementById('dentistPhone');");

out.println("const error = document.getElementById('dentistPhoneError');");

out.println("const value = phone.value.trim();");

out.println("error.textContent = '';");

out.println("phone.classList.remove('input-error');");

out.println("if (value === '') {");

out.println("event.preventDefault();");

out.println("error.textContent = 'Phone number is required.';");

out.println("phone.classList.add('input-error');");

out.println("return;");

out.println("}");

out.println("if (!/^[0-9]{10}$/.test(value)) {");

out.println("event.preventDefault();");

out.println("error.textContent = 'Phone number must contain exactly 10 digits.';");

out.println("phone.classList.add('input-error');");

out.println("return;");

out.println("}");

out.println("});");

out.println("</script>");


out.println("<script src='js/role.js'></script>");
        out.println("</body>");

out.println("</html>");

out.println("</div>");



out.println("</form>");



out.println("</div>");



out.println("</div>");



out.println("<script src='js/role.js'></script>");
        out.println("</body>");



out.println("</html>");

    }
}
