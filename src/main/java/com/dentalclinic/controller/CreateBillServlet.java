package com.dentalclinic.controller;

import com.dentalclinic.dao.BillDAO;
import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Bill;
import com.dentalclinic.model.Appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/CreateBillServlet")
public class CreateBillServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BillDAO billDAO;
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        billDAO = new BillDAO();
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/create-bill.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String appointmentValue = request.getParameter("appointmentId");
            String chargeValue = request.getParameter("treatmentCharge");
            String discountValue = request.getParameter("discount");
            String paidValue = request.getParameter("amountPaid");

            if (appointmentValue == null || chargeValue == null
                    || discountValue == null || paidValue == null) {
                redirectWithError(request, response, "Please complete all bill fields.");
                return;
            }

            int appointmentId = Integer.parseInt(appointmentValue);
            double treatmentCharge = Double.parseDouble(chargeValue);
            double discount = Double.parseDouble(discountValue);
            double amountPaid = Double.parseDouble(paidValue);

            if (appointmentId <= 0 || treatmentCharge < 0 || discount < 0 || amountPaid < 0) {
                redirectWithError(request, response, "Please enter valid positive values.");
                return;
            }

            if (discount > treatmentCharge) {
                redirectWithError(request, response, "Discount cannot be greater than the treatment charge.");
                return;
            }

            double totalAmount = treatmentCharge - discount;
            double balance = Math.max(totalAmount - amountPaid, 0);

            String status;
            if (amountPaid <= 0) {
                status = "Unpaid";
            } else if (amountPaid >= totalAmount) {
                status = "Paid";
            } else {
                status = "Partial";
            }

            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);

            if (appointment == null) {
                redirectWithError(request, response, "Appointment ID " + appointmentId + " was not found.");
                return;
            }

            Bill bill = new Bill();
            bill.setAppointmentId(appointmentId);
            bill.setTreatmentCharge(treatmentCharge);
            bill.setDiscount(discount);
            bill.setTotalAmount(totalAmount);
            bill.setAmountPaid(amountPaid);
            bill.setBalance(balance);
            bill.setStatus(status);

            boolean success = billDAO.createBill(bill);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/billing.html?success=1");
            } else {
                redirectWithError(request, response, "Bill could not be saved. Check the database and bills table.");
            }

        } catch (NumberFormatException e) {
            redirectWithError(request, response, "Please enter valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithError(request, response, "An error occurred while creating the bill.");
        }
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/create-bill.html?error="
                + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8));
    }
}
