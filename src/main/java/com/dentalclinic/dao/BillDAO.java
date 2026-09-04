/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.Bill;
import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author hasin
 */
public class BillDAO {
    
    public boolean addBill(Bill bill) {

        String sql = "INSERT INTO bills "
                + "(appointment_id, patient_id, dentist_id, "
                + "treatment_charge, discount, total_amount, "
                + "amount_paid, balance, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getAppointmentId());
            statement.setInt(2, bill.getPatientId());
            statement.setInt(3, bill.getDentistId());
            statement.setDouble(4, bill.getTreatmentCharge());
            statement.setDouble(5, bill.getDiscount());
            statement.setDouble(6, bill.getTotalAmount());
            statement.setDouble(7, bill.getAmountPaid());
            statement.setDouble(8, bill.getBalance());
            statement.setString(9, bill.getStatus());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
         
    }
    
    public boolean createBill(Bill bill) {

        // Get patient_id and dentist_id from the selected appointment.
        // This keeps the Create Bill form simple: it only needs appointment_id.
        String sql = "INSERT INTO bills "
                + "(appointment_id, patient_id, dentist_id, treatment_charge, "
                + "discount, total_amount, amount_paid, balance, status) "
                + "SELECT a.appointment_id, a.patient_id, a.dentist_id, ?, ?, ?, ?, ?, ? "
                + "FROM appointments a WHERE a.appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, bill.getTreatmentCharge());
            ps.setDouble(2, bill.getDiscount());
            ps.setDouble(3, bill.getTotalAmount());
            ps.setDouble(4, bill.getAmountPaid());
            ps.setDouble(5, bill.getBalance());
            ps.setString(6, bill.getStatus());
            ps.setInt(7, bill.getAppointmentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();

        String sql = "SELECT b.*, " +
                "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, " +
                "d.dentist_name AS dentist_name " +
                "FROM bills b " +
                "LEFT JOIN patients p ON b.patient_id = p.patient_id " +
                "LEFT JOIN dentists d ON b.dentist_id = d.dentist_id " +
                "ORDER BY b.bill_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setBillId(resultSet.getInt("bill_id"));
                bill.setAppointmentId(resultSet.getInt("appointment_id"));
                bill.setPatientId(resultSet.getInt("patient_id"));
                bill.setDentistId(resultSet.getInt("dentist_id"));
                bill.setPatientName(resultSet.getString("patient_name"));
                bill.setDentistName(resultSet.getString("dentist_name"));
                bill.setTreatmentCharge(resultSet.getDouble("treatment_charge"));
                bill.setDiscount(resultSet.getDouble("discount"));
                bill.setTotalAmount(resultSet.getDouble("total_amount"));
                bill.setAmountPaid(resultSet.getDouble("amount_paid"));
                bill.setBalance(resultSet.getDouble("balance"));
                bill.setStatus(resultSet.getString("status"));
                bill.setCreatedAt(resultSet.getTimestamp("created_at"));
                bills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }


    public Bill getBillById(int billId) {

        Bill bill = null;

        String sql = "SELECT b.*, " +
                "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, " +
                "d.dentist_name AS dentist_name " +
                "FROM bills b " +
                "LEFT JOIN patients p ON b.patient_id = p.patient_id " +
                "LEFT JOIN dentists d ON b.dentist_id = d.dentist_id " +
                "WHERE b.bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    bill = new Bill();

                    bill.setBillId(
                            resultSet.getInt("bill_id")
                    );

                    bill.setAppointmentId(
                            resultSet.getInt("appointment_id")
                    );

                    bill.setPatientId(
                            resultSet.getInt("patient_id")
                    );

                    bill.setDentistId(
                            resultSet.getInt("dentist_id")
                    );

                    bill.setPatientName(resultSet.getString("patient_name"));
                    bill.setDentistName(resultSet.getString("dentist_name"));

                    bill.setTreatmentCharge(
                            resultSet.getDouble("treatment_charge")
                    );

                    bill.setDiscount(
                            resultSet.getDouble("discount")
                    );

                    bill.setTotalAmount(
                            resultSet.getDouble("total_amount")
                    );

                    bill.setAmountPaid(
                            resultSet.getDouble("amount_paid")
                    );

                    bill.setBalance(
                            resultSet.getDouble("balance")
                    );

                    bill.setStatus(
                            resultSet.getString("status")
                    );

                    bill.setCreatedAt(
                            resultSet.getTimestamp("created_at")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bill;
    }



    public boolean updateBill(Bill bill) {

        String sql = "UPDATE bills SET "
                + "appointment_id = ?, "
                + "patient_id = ?, "
                + "dentist_id = ?, "
                + "treatment_charge = ?, "
                + "discount = ?, "
                + "total_amount = ?, "
                + "amount_paid = ?, "
                + "balance = ?, "
                + "status = ? "
                + "WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getAppointmentId());
            statement.setInt(2, bill.getPatientId());
            statement.setInt(3, bill.getDentistId());
            statement.setDouble(4, bill.getTreatmentCharge());
            statement.setDouble(5, bill.getDiscount());
            statement.setDouble(6, bill.getTotalAmount());
            statement.setDouble(7, bill.getAmountPaid());
            statement.setDouble(8, bill.getBalance());
            statement.setString(9, bill.getStatus());
            statement.setInt(10, bill.getBillId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


   
    public boolean deleteBill(int billId) {

        String sql = "DELETE FROM bills WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<Bill> getBillsForDentist(int dentistId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT b.*, CONCAT(p.first_name, ' ', p.last_name) AS patient_name, "
                + "d.dentist_name AS dentist_name FROM bills b "
                + "LEFT JOIN patients p ON b.patient_id = p.patient_id "
                + "LEFT JOIN dentists d ON b.dentist_id = d.dentist_id "
                + "WHERE b.dentist_id = ? ORDER BY b.bill_id DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dentistId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAppointmentId(rs.getInt("appointment_id"));
                    bill.setPatientId(rs.getInt("patient_id"));
                    bill.setDentistId(rs.getInt("dentist_id"));
                    bill.setPatientName(rs.getString("patient_name"));
                    bill.setDentistName(rs.getString("dentist_name"));
                    bill.setTreatmentCharge(rs.getDouble("treatment_charge"));
                    bill.setDiscount(rs.getDouble("discount"));
                    bill.setTotalAmount(rs.getDouble("total_amount"));
                    bill.setAmountPaid(rs.getDouble("amount_paid"));
                    bill.setBalance(rs.getDouble("balance"));
                    bill.setStatus(rs.getString("status"));
                    bill.setCreatedAt(rs.getTimestamp("created_at"));
                    bills.add(bill);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return bills;
    }

    public boolean billBelongsToDentist(int billId, int dentistId) {
        String sql = "SELECT COUNT(*) FROM bills WHERE bill_id = ? AND dentist_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            statement.setInt(2, dentistId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

}
    

