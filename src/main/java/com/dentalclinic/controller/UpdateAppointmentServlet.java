/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Appointment;

import java.io.IOException;

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
    
    @WebServlet("/UpdateAppointmentServlet")
public class UpdateAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Appointment appointment = new Appointment();

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        if (RoleAccess.isDentist(request)) {
            Integer dentistId = RoleAccess.dentistId(request);
            if (dentistId == null || !new AppointmentDAO().appointmentBelongsToDentist(appointmentId, dentistId)) {
                response.sendError(403, "You can update only your own appointments.");
                return;
            }
        }
        appointment.setAppointmentId(
                Integer.parseInt(request.getParameter("appointmentId")));

        appointment.setPatientId(
                Integer.parseInt(request.getParameter("patientId")));

        if (RoleAccess.isDentist(request)) {
            appointment.setDentistId(RoleAccess.dentistId(request));
        } else {
            appointment.setDentistId(Integer.parseInt(request.getParameter("dentistId")));
        }

        appointment.setAppointmentDate(
                request.getParameter("appointmentDate"));

        appointment.setAppointmentTime(
                request.getParameter("appointmentTime"));

        appointment.setTreatment(
                request.getParameter("treatment"));

        appointment.setStatus(
                request.getParameter("status"));

        AppointmentDAO dao = new AppointmentDAO();

        dao.updateAppointment(appointment);

        response.sendRedirect("ViewAppointmentsServlet");
    }
}
    

