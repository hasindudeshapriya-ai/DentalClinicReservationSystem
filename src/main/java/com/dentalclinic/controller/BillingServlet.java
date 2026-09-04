package com.dentalclinic.controller;

import com.dentalclinic.dao.BillDAO;
import com.dentalclinic.model.Bill;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.dentalclinic.util.RoleAccess;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/BillingServlet")
public class BillingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Bill> bills;
        if (RoleAccess.isDentist(request)) {
            Integer dentistId = RoleAccess.dentistId(request);
            if (dentistId == null) { response.sendError(403, "Dentist profile is not linked."); return; }
            bills = billDAO.getBillsForDentist(dentistId);
        } else {
            bills = billDAO.getAllBills();
        }

        if ("json".equalsIgnoreCase(request.getParameter("format"))) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("[");

            for (int i = 0; i < bills.size(); i++) {
                Bill b = bills.get(i);
                if (i > 0) out.print(",");
                out.print("{");
                out.print("\"billId\":" + b.getBillId() + ",");
                out.print("\"appointmentId\":" + b.getAppointmentId() + ",");
                out.print("\"patientId\":" + b.getPatientId() + ",");
                out.print("\"dentistId\":" + b.getDentistId() + ",");
                out.print("\"patientName\":\"" + jsonEscape(b.getPatientName()) + "\",");
                out.print("\"dentistName\":\"" + jsonEscape(b.getDentistName()) + "\",");
                out.print("\"treatmentCharge\":" + b.getTreatmentCharge() + ",");
                out.print("\"discount\":" + b.getDiscount() + ",");
                out.print("\"totalAmount\":" + b.getTotalAmount() + ",");
                out.print("\"amountPaid\":" + b.getAmountPaid() + ",");
                out.print("\"balance\":" + b.getBalance() + ",");
                out.print("\"status\":\"" + jsonEscape(b.getStatus()) + "\",");
                out.print("\"createdDate\":\"" +
                        jsonEscape(b.getCreatedAt() == null ? "" : b.getCreatedAt().toString()) + "\"");
                out.print("}");
            }

            out.print("]");
            return;
        }

        if (RoleAccess.isDentist(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Dentists have view-only access to their own bills and payments.");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/billing.html");
    }

    private String jsonEscape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/billing.html");
    }
}
