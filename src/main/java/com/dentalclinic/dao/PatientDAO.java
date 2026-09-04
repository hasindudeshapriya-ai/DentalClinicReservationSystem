/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.Patient;
import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author hasin
 */
public class PatientDAO {
    
   public boolean savePatient(Patient patient) {

        boolean status = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO patients(first_name,last_name,gender,date_of_birth,phone,email,address) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getDateOfBirth());
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
        
        
    } 
   
   public List<Patient> getAllPatients() {

    List<Patient> patientList = new ArrayList<>();

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM patients ORDER BY patient_id DESC";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Patient patient = new Patient();

            patient.setPatientId(rs.getInt("patient_id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setGender(rs.getString("gender"));
            patient.setDateOfBirth(rs.getString("date_of_birth"));
            patient.setPhone(rs.getString("phone"));
            patient.setEmail(rs.getString("email"));
            patient.setAddress(rs.getString("address"));

            patientList.add(patient);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return patientList;
  }
   
   public Patient getPatientById(int patientId) {

    Patient patient = null;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, patientId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            patient = new Patient();

            patient.setPatientId(rs.getInt("patient_id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setGender(rs.getString("gender"));
            patient.setDateOfBirth(rs.getString("date_of_birth"));
            patient.setPhone(rs.getString("phone"));
            patient.setEmail(rs.getString("email"));
            patient.setAddress(rs.getString("address"));

        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return patient;
  }
   
   public boolean updatePatient(Patient patient) {

    boolean status = false;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "UPDATE patients SET first_name=?, last_name=?, gender=?, date_of_birth=?, phone=?, email=?, address=? WHERE patient_id=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, patient.getFirstName());
        ps.setString(2, patient.getLastName());
        ps.setString(3, patient.getGender());
        ps.setString(4, patient.getDateOfBirth());
        ps.setString(5, patient.getPhone());
        ps.setString(6, patient.getEmail());
        ps.setString(7, patient.getAddress());
        ps.setInt(8, patient.getPatientId());

        int rows = ps.executeUpdate();

        if (rows > 0) {
            status = true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return status;
   }
  public boolean deletePatient(int patientId) {

    boolean status = false;

    try {

        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, patientId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            status = true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return status;
   }
 
   public List<Patient> getPatientsForDentist(int dentistId) {
    List<Patient> patientList = new ArrayList<>();
    String sql = "SELECT DISTINCT p.* FROM patients p "
            + "INNER JOIN appointments a ON p.patient_id = a.patient_id "
            + "WHERE a.dentist_id = ? ORDER BY p.patient_id DESC";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, dentistId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("patient_id"));
                patient.setFirstName(rs.getString("first_name"));
                patient.setLastName(rs.getString("last_name"));
                patient.setGender(rs.getString("gender"));
                patient.setDateOfBirth(rs.getString("date_of_birth"));
                patient.setPhone(rs.getString("phone"));
                patient.setEmail(rs.getString("email"));
                patient.setAddress(rs.getString("address"));
                patientList.add(patient);
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return patientList;
   }

   public boolean patientBelongsToDentist(int patientId, int dentistId) {
    String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND dentist_id = ?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, patientId);
        ps.setInt(2, dentistId);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    } catch (Exception e) { e.printStackTrace(); return false; }
   }

}
