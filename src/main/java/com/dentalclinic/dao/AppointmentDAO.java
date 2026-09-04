/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.Appointment;
import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.dentalclinic.model.Patient;
import com.dentalclinic.model.Dentist;

/**
 *
 * @author hasin
 */
public class AppointmentDAO {
    
    public boolean saveAppointment(Appointment appointment) {

        boolean status = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO appointments(patient_id, dentist_id, appointment_date, appointment_time, treatment, status) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDentistId());
            ps.setString(3, appointment.getAppointmentDate());
            ps.setString(4, appointment.getAppointmentTime());
            ps.setString(5, appointment.getTreatment());
            ps.setString(6, appointment.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
    
    public List<Appointment> getAllAppointments() {

    List<Appointment> appointments = new ArrayList<>();

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT a.appointment_id, "
                + "p.first_name, "
                + "p.last_name, "
                + "d.dentist_name, "
                + "a.appointment_date, "
                + "a.appointment_time, "
                + "a.treatment, "
                + "a.status "
                + "FROM appointments a "
                + "INNER JOIN patients p ON a.patient_id = p.patient_id "
                + "INNER JOIN dentists d ON a.dentist_id = d.dentist_id "
                + "ORDER BY a.appointment_date, a.appointment_time";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Appointment appointment = new Appointment();

            appointment.setAppointmentId(rs.getInt("appointment_id"));

            appointment.setPatientName(
                    rs.getString("first_name") + " " + rs.getString("last_name")
            );

            appointment.setDentistName(
                    rs.getString("dentist_name")
            );

            appointment.setAppointmentDate(
                    rs.getString("appointment_date")
            );

            appointment.setAppointmentTime(
                    rs.getString("appointment_time")
            );

            appointment.setTreatment(
                    rs.getString("treatment")
            );

            appointment.setStatus(
                    rs.getString("status")
            );

            appointments.add(appointment);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return appointments;
  }
    
  public Appointment getAppointmentById(int appointmentId) {

    Appointment appointment = null;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, appointmentId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            appointment = new Appointment();

            appointment.setAppointmentId(rs.getInt("appointment_id"));
            appointment.setPatientId(rs.getInt("patient_id"));
            appointment.setDentistId(rs.getInt("dentist_id"));
            appointment.setAppointmentDate(rs.getString("appointment_date"));
            appointment.setAppointmentTime(rs.getString("appointment_time"));
            appointment.setTreatment(rs.getString("treatment"));
            appointment.setStatus(rs.getString("status"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return appointment;
  } 
  
  public boolean deleteAppointment(int appointmentId) {

    boolean deleted = false;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, appointmentId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            deleted = true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return deleted;
}
  public List<Dentist> getAllDentists() {

    List<Dentist> dentists = new ArrayList<>();

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT dentist_id, dentist_name FROM dentists ORDER BY dentist_name";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Dentist dentist = new Dentist();

            dentist.setDentistId(rs.getInt("dentist_id"));
            dentist.setDentistName(rs.getString("dentist_name"));

            dentists.add(dentist);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return dentists;
}
  public List<Patient> getAllPatients() {

    List<Patient> patients = new ArrayList<>();

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT patient_id, first_name, last_name FROM patients ORDER BY first_name";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Patient patient = new Patient();

            patient.setPatientId(rs.getInt("patient_id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));

            patients.add(patient);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return patients;
}
  
  public boolean updateAppointment(Appointment appointment) {

    boolean updated = false;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "UPDATE appointments SET "
                + "patient_id=?, "
                + "dentist_id=?, "
                + "appointment_date=?, "
                + "appointment_time=?, "
                + "treatment=?, "
                + "status=? "
                + "WHERE appointment_id=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, appointment.getPatientId());
        ps.setInt(2, appointment.getDentistId());
        ps.setString(3, appointment.getAppointmentDate());
        ps.setString(4, appointment.getAppointmentTime());
        ps.setString(5, appointment.getTreatment());
        ps.setString(6, appointment.getStatus());
        ps.setInt(7, appointment.getAppointmentId());

        int rows = ps.executeUpdate();

        if (rows > 0) {
            updated = true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return updated;
  }
   
  public List<Appointment> getAppointmentsForDentist(int dentistId) {
    List<Appointment> appointments = new ArrayList<>();
    String sql = "SELECT a.appointment_id, p.first_name, p.last_name, d.dentist_name, "
            + "a.appointment_date, a.appointment_time, a.treatment, a.status "
            + "FROM appointments a "
            + "INNER JOIN patients p ON a.patient_id = p.patient_id "
            + "INNER JOIN dentists d ON a.dentist_id = d.dentist_id "
            + "WHERE a.dentist_id = ? "
            + "ORDER BY a.appointment_date, a.appointment_time";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, dentistId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientName(rs.getString("first_name") + " " + rs.getString("last_name"));
                a.setDentistName(rs.getString("dentist_name"));
                a.setAppointmentDate(rs.getString("appointment_date"));
                a.setAppointmentTime(rs.getString("appointment_time"));
                a.setTreatment(rs.getString("treatment"));
                a.setStatus(rs.getString("status"));
                appointments.add(a);
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return appointments;
  }

  public boolean appointmentBelongsToDentist(int appointmentId, int dentistId) {
    String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_id = ? AND dentist_id = ?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, appointmentId);
        ps.setInt(2, dentistId);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    } catch (Exception e) { e.printStackTrace(); return false; }
  }

}
    

