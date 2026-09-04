package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.Patient;
import com.dentalclinic.model.Dentist;
import com.dentalclinic.util.RoleAccess;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AppointmentServlet")
public class AppointmentServlet extends HttpServlet {

    private final Gson gson = new Gson();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            AppointmentDAO dao = new AppointmentDAO();

            List<Patient> patients =
                    dao.getAllPatients();

            List<Dentist> dentists =
                    dao.getAllDentists();

            Map<String, Object> result =
                    new HashMap<>();

            result.put("success", true);
            result.put("patients", patients);
            result.put("dentists", dentists);

            response.getWriter().write(
                    gson.toJson(result)
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            Map<String, Object> error =
                    new HashMap<>();

            error.put("success", false);
            error.put(
                    "message",
                    "Unable to load patients and dentists."
            );

            response.getWriter().write(
                    gson.toJson(error)
            );
        }
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String patientIdParam =
                    request.getParameter("patientId");

            if (patientIdParam == null
                    || patientIdParam.trim().isEmpty()) {

                response.sendRedirect(
                        "appointment.html?error=patient"
                );

                return;
            }

            Appointment appointment =
                    new Appointment();

            appointment.setPatientId(
                    Integer.parseInt(patientIdParam)
            );


            if (RoleAccess.isDentist(request)) {

                Integer ownDentistId =
                        RoleAccess.dentistId(request);

                if (ownDentistId == null) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Dentist profile is not linked."
                    );

                    return;
                }

                appointment.setDentistId(
                        ownDentistId
                );

            } else {

                String dentistIdParam =
                        request.getParameter("dentistId");

                if (dentistIdParam == null
                        || dentistIdParam.trim().isEmpty()) {

                    response.sendRedirect(
                            "appointment.html?error=dentist"
                    );

                    return;
                }

                appointment.setDentistId(
                        Integer.parseInt(dentistIdParam)
                );
            }


            appointment.setAppointmentDate(
                    request.getParameter(
                            "appointmentDate"
                    )
            );

            appointment.setAppointmentTime(
                    request.getParameter(
                            "appointmentTime"
                    )
            );

            appointment.setTreatment(
                    request.getParameter(
                            "treatment"
                    )
            );

            appointment.setStatus(
                    request.getParameter(
                            "status"
                    )
            );


            AppointmentDAO dao =
                    new AppointmentDAO();

            boolean status =
                    dao.saveAppointment(
                            appointment
                    );

            if (status) {

                response.sendRedirect(
                        "appointment.html?success=1"
                );

            } else {

                response.sendRedirect(
                        "appointment.html?error=1"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "appointment.html?error=1"
            );
        }
    }
}