/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hasin
 */
public class ReportDAO {
    public Map<String, Object> getReportData() {

        Map<String, Object> report = new LinkedHashMap<>();

        try {

            Connection con = DBConnection.getConnection();


            Map<String, Object> appointments = new LinkedHashMap<>();

            String appointmentSql =
                    "SELECT "
                    + "COUNT(*) AS total, "
                    + "SUM(CASE WHEN LOWER(status) = 'completed' THEN 1 ELSE 0 END) AS completed, "
                    + "SUM(CASE WHEN LOWER(status) IN ('scheduled', 'pending') THEN 1 ELSE 0 END) AS pending, "
                    + "SUM(CASE WHEN LOWER(status) = 'cancelled' THEN 1 ELSE 0 END) AS cancelled "
                    + "FROM appointments";

            PreparedStatement appointmentPs =
                    con.prepareStatement(appointmentSql);

            ResultSet appointmentRs =
                    appointmentPs.executeQuery();

            if (appointmentRs.next()) {

                appointments.put(
                        "total",
                        appointmentRs.getInt("total")
                );

                appointments.put(
                        "completed",
                        appointmentRs.getInt("completed")
                );

                appointments.put(
                        "pending",
                        appointmentRs.getInt("pending")
                );

                appointments.put(
                        "cancelled",
                        appointmentRs.getInt("cancelled")
                );
            }

            report.put("appointments", appointments);



            String patientSql =
                    "SELECT COUNT(*) AS total FROM patients";

            PreparedStatement patientPs =
                    con.prepareStatement(patientSql);

            ResultSet patientRs =
                    patientPs.executeQuery();

            int totalPatients = 0;

            if (patientRs.next()) {
                totalPatients =
                        patientRs.getInt("total");
            }

            report.put(
                    "totalPatients",
                    totalPatients
            );


            Map<String, Object> billing =
                    new LinkedHashMap<>();

            String billingSql =
                    "SELECT "
                    + "COALESCE(SUM(treatment_charge), 0) AS treatment_charges, "
                    + "COALESCE(SUM(discount), 0) AS discounts, "
                    + "COALESCE(SUM(total_amount), 0) AS total_bill_amount, "
                    + "COALESCE(SUM(amount_paid), 0) AS total_paid, "
                    + "COALESCE(SUM(balance), 0) AS outstanding_balance "
                    + "FROM bills";

            PreparedStatement billingPs =
                    con.prepareStatement(billingSql);

            ResultSet billingRs =
                    billingPs.executeQuery();

            if (billingRs.next()) {

                billing.put(
                        "treatmentCharges",
                        billingRs.getDouble("treatment_charges")
                );

                billing.put(
                        "discounts",
                        billingRs.getDouble("discounts")
                );

                billing.put(
                        "totalBillAmount",
                        billingRs.getDouble("total_bill_amount")
                );

                billing.put(
                        "totalPaid",
                        billingRs.getDouble("total_paid")
                );

                billing.put(
                        "outstandingBalance",
                        billingRs.getDouble("outstanding_balance")
                );

                billing.put(
                        "totalRevenue",
                        billingRs.getDouble("total_paid")
                );
            }

            report.put(
                    "billing",
                    billing
            );

         

            List<Map<String, Object>> recentBills =
                    new ArrayList<>();

            String recentBillsSql =
                    "SELECT "
                    + "b.bill_id, "
                    + "b.appointment_id, "
                    + "b.treatment_charge, "
                    + "b.discount, "
                    + "b.total_amount, "
                    + "b.amount_paid, "
                    + "b.balance, "
                    + "b.status, "
                    + "b.created_at, "
                    + "p.first_name, "
                    + "p.last_name, "
                    + "d.dentist_name "
                    + "FROM bills b "
                    + "INNER JOIN appointments a "
                    + "ON b.appointment_id = a.appointment_id "
                    + "INNER JOIN patients p "
                    + "ON a.patient_id = p.patient_id "
                    + "INNER JOIN dentists d "
                    + "ON a.dentist_id = d.dentist_id "
                    + "ORDER BY b.created_at DESC "
                    + "LIMIT 10";

            PreparedStatement recentPs =
                    con.prepareStatement(recentBillsSql);

            ResultSet recentRs =
                    recentPs.executeQuery();

            while (recentRs.next()) {

                Map<String, Object> bill =
                        new LinkedHashMap<>();

                bill.put(
                        "billId",
                        recentRs.getInt("bill_id")
                );

                bill.put(
                        "appointmentId",
                        recentRs.getInt("appointment_id")
                );

                bill.put(
                        "patientName",
                        recentRs.getString("first_name")
                        + " "
                        + recentRs.getString("last_name")
                );

                bill.put(
                        "dentistName",
                        recentRs.getString("dentist_name")
                );

                bill.put(
                        "treatmentCharge",
                        recentRs.getDouble("treatment_charge")
                );

                bill.put(
                        "discount",
                        recentRs.getDouble("discount")
                );

                bill.put(
                        "totalAmount",
                        recentRs.getDouble("total_amount")
                );

                bill.put(
                        "amountPaid",
                        recentRs.getDouble("amount_paid")
                );

                bill.put(
                        "balance",
                        recentRs.getDouble("balance")
                );

                bill.put(
                        "status",
                        recentRs.getString("status")
                );

                if (recentRs.getTimestamp("created_at") != null) {

                    bill.put(
                            "createdAt",
                            recentRs.getTimestamp("created_at").toString()
                    );

                } else {

                    bill.put(
                            "createdAt",
                            ""
                    );
                }

                recentBills.add(bill);
            }

            report.put(
                    "recentBills",
                    recentBills
            );


            

            recentRs.close();
            recentPs.close();

            billingRs.close();
            billingPs.close();

            patientRs.close();
            patientPs.close();

            appointmentRs.close();
            appointmentPs.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            /*
             * Keep the application from completely
             * crashing if report data cannot load.
             */

            report.put(
                    "error",
                    "Unable to load report data"
            );
        }

        return report;
    }
}
    

