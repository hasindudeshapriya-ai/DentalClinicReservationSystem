package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.Patient;
import com.dentalclinic.model.Dentist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.dentalclinic.util.RoleAccess;

@WebServlet("/EditAppointmentServlet")
public class EditAppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        int appointmentId = Integer.parseInt(request.getParameter("id"));
        if (RoleAccess.isDentist(request)) {
            Integer dentistId = RoleAccess.dentistId(request);
            if (dentistId == null || !new AppointmentDAO().appointmentBelongsToDentist(appointmentId, dentistId)) {
                response.sendError(403, "You can edit only your own appointments.");
                return;
            }
        }

        AppointmentDAO dao = new AppointmentDAO();

       
        Appointment appointment = dao.getAppointmentById(appointmentId);

        
        List<Patient> patients = dao.getAllPatients();

       
        List<Dentist> dentists = dao.getAllDentists();

        
        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<title>Edit Appointment</title>");
        // Use relative paths because this project is deployed with a
        // Tomcat context path that may differ from the Maven artifact name.
        // Relative paths resolve correctly from the servlet URL.
        out.println("<meta charset='UTF-8'>");
        String contextPath = request.getContextPath();
        out.println("<link rel='stylesheet' href='" + contextPath + "/css/dashboard.css?v=3'>");
        out.println("<link rel='stylesheet' href='" + contextPath + "/css/appointment.css?v=3'>");
out.println("<link rel=\'stylesheet\' href=\'css/clinic-background.css\'>");

        // Inline styles are intentionally included here so the Edit Appointment
        // page remains styled even if Tomcat/browser caching prevents the
        // external appointment.css file from loading.
        out.println("<style>");
        out.println("*{box-sizing:border-box;font-family:Arial,sans-serif;margin:0;padding:0;}");
        out.println("body{display:flex;background:#f4f6f9;min-height:100vh;color:#111;}");
        out.println(".sidebar{width:250px;min-width:250px;height:100vh;background:#1565c0;padding:20px;color:#fff;}");
        out.println(".sidebar h2{margin-bottom:30px;color:#fff;font-size:26px;}");
        out.println(".sidebar a{display:block;color:#fff;text-decoration:none;margin:12px 0;padding:12px;border-radius:7px;font-size:16px;}");
        out.println(".sidebar a:hover,.sidebar a.active{background:#0d47a1;}");
        out.println(".main{flex:1;padding:30px;min-width:0;}");
        out.println(".topbar{display:flex;justify-content:space-between;align-items:center;margin-bottom:20px;}");
        out.println(".topbar h1{font-size:36px;margin:0;}");
        out.println(".topbar h3{font-size:20px;margin:0;}");
        out.println(".edit-appointment-form{background:#fff;width:100%;padding:34px;border-radius:14px;box-shadow:0 5px 18px rgba(0,0,0,.10);margin-top:20px;}");
        out.println(".form-row{display:grid;grid-template-columns:1fr 1fr;gap:24px;margin-bottom:18px;}");
        out.println(".form-group{width:100%;margin-bottom:18px;}");
        out.println(".form-group label{display:block;margin-bottom:8px;font-size:16px;font-weight:600;color:#222;}");
        out.println(".form-group input,.form-group select{display:block;width:100%;height:48px;padding:10px 14px;border:1px solid #d5d5d5;border-radius:8px;font-size:16px;background:#fff;box-sizing:border-box;}");
        out.println(".form-group input:focus,.form-group select:focus{outline:none;border-color:#1565c0;box-shadow:0 0 0 2px rgba(21,101,192,.12);}");
        out.println(".button-group{display:flex;gap:14px;margin-top:24px;align-items:center;}");
        out.println(".update-btn{border:0;background:#1565c0;color:#fff;padding:13px 26px;border-radius:7px;font-size:16px;font-weight:600;cursor:pointer;}");
        out.println(".update-btn:hover{background:#0d47a1;}");
        out.println(".cancel-btn{display:inline-block;background:#777;color:#fff;text-decoration:none;padding:13px 26px;border-radius:7px;font-size:16px;font-weight:600;}");
        out.println(".cancel-btn:hover{background:#555;color:#fff;}");
        out.println("@media(max-width:768px){body{display:block}.sidebar{width:100%;min-width:0;height:auto}.main{padding:20px}.form-row{grid-template-columns:1fr;gap:0}.button-group{flex-direction:column;align-items:stretch}.update-btn,.cancel-btn{text-align:center;width:100%;}}");
        out.println("</style>");
        out.println("</head>");

        out.println("<body>");


out.println("<div class='sidebar'>");

out.println("<h2>🦷 Dental Clinic</h2>");

out.println("<a href='dashboard.html'>🏠 Dashboard</a>");
out.println("<a href='patients.html'>👤 Patients</a>");
out.println("<a href='appointment.html' class='active'>📅 Appointments</a>");
out.println("<a href='#'>🦷 Dentists</a>");
out.println("<a href='#'>💳 Billing</a>");
out.println("<a href='#'>📊 Reports</a>");
out.println("<a href='#'>👥 User Management</a>");
out.println("<a href='index.html'>🚪 Logout</a>");

out.println("</div>");



out.println("<div class='main'>");

out.println("<div class='topbar'>");
out.println("<h1>Edit Appointment</h1>");
out.println("<h3>Update Appointment Details</h3>");
out.println("</div>");



out.println("<div class='edit-appointment-form'>");

out.println("<form action='UpdateAppointmentServlet' method='post'>");


out.println("<input type='hidden' name='appointmentId' value='"
        + appointment.getAppointmentId() + "'>");

out.println("<div class='form-row'>");

out.println("<div class='form-group'>");
out.println("<label>Patient</label>");
out.println("<select name='patientId' required>");

for (Patient patient : patients) {

    if (patient.getPatientId() == appointment.getPatientId()) {

        out.println("<option value='" + patient.getPatientId()
                + "' selected>"
                + patient.getFirstName() + " "
                + patient.getLastName()
                + "</option>");

    } else {

        out.println("<option value='" + patient.getPatientId()
                + "'>"
                + patient.getFirstName() + " "
                + patient.getLastName()
                + "</option>");
    }
}

out.println("</select>");
out.println("</div>");


out.println("<div class='form-group'>");
out.println("<label>Dentist</label>");
out.println("<select name='dentistId' required>");

for (Dentist dentist : dentists) {

    if (dentist.getDentistId() == appointment.getDentistId()) {

        out.println("<option value='" + dentist.getDentistId()
                + "' selected>"
                + dentist.getDentistName()
                + "</option>");

    } else {

        out.println("<option value='" + dentist.getDentistId()
                + "'>"
                + dentist.getDentistName()
                + "</option>");
    }
}

out.println("</select>");
out.println("</div>");

out.println("</div>");

out.println("<div class='form-row'>");

out.println("<div class='form-group'>");
out.println("<label>Appointment Date</label>");
out.println("<input type='date' name='appointmentDate' value='"
        + appointment.getAppointmentDate()
        + "' required>");
out.println("</div>");


out.println("<div class='form-group'>");
out.println("<label>Appointment Time</label>");
out.println("<input type='time' name='appointmentTime' value='"
        + appointment.getAppointmentTime()
        + "' required>");
out.println("</div>");

out.println("</div>");

out.println("<div class='form-group full-width'>");

out.println("<label>Treatment</label>");

out.println("<input type='text' "
        + "name='treatment' "
        + "value='" + appointment.getTreatment() + "' "
        + "required>");

out.println("</div>");

out.println("<div class='form-group full-width'>");

out.println("<label>Status</label>");

out.println("<select name='status' required>");

out.println("<option value='Scheduled' "
        + (appointment.getStatus().equals("Scheduled")
        ? "selected" : "")
        + ">Scheduled</option>");

out.println("<option value='Completed' "
        + (appointment.getStatus().equals("Completed")
        ? "selected" : "")
        + ">Completed</option>");

out.println("<option value='Cancelled' "
        + (appointment.getStatus().equals("Cancelled")
        ? "selected" : "")
        + ">Cancelled</option>");

out.println("</select>");

out.println("</div>");

        
out.println("<div class='button-group'>");

out.println("<button type='submit' class='update-btn'>"
        + "Update Appointment"
        + "</button>");

out.println("<a href='ViewAppointmentsServlet' class='cancel-btn'>"
        + "Cancel"
        + "</a>");

out.println("</div>");

out.println("</form>");


out.println("</div>");


out.println("</div>");

out.println("<script src='js/role.js'></script>");
out.println("</body>");
out.println("</html>");
    }
}