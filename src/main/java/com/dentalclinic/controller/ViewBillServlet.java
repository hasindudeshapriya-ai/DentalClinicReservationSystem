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

@WebServlet("/ViewBillServlet")
public class ViewBillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String value = request.getParameter("id");
        if (value == null || value.trim().isEmpty()) {
            sendError(response, "Bill ID is required.");
            return;
        }

        try {
            int billId = Integer.parseInt(value);
            if (RoleAccess.isDentist(request)) {
                Integer dentistId = RoleAccess.dentistId(request);
                if (dentistId == null || !billDAO.billBelongsToDentist(billId, dentistId)) {
                    sendError(response, "You can view only your own bills.");
                    return;
                }
            }

            Bill b = billDAO.getBillById(billId);

            if (b == null) {
                sendError(response, "Bill not found.");
                return;
            }

            PrintWriter out = response.getWriter();
            out.print("{");
            out.print("\"billId\":" + b.getBillId() + ",");
            out.print("\"appointmentId\":" + b.getAppointmentId() + ",");
            out.print("\"patientId\":" + b.getPatientId() + ",");
            out.print("\"dentistId\":" + b.getDentistId() + ",");
            out.print("\"patientName\":\"" + esc(b.getPatientName()) + "\",");
            out.print("\"dentistName\":\"" + esc(b.getDentistName()) + "\",");
            out.print("\"treatmentCharge\":" + b.getTreatmentCharge() + ",");
            out.print("\"discount\":" + b.getDiscount() + ",");
            out.print("\"total\":" + b.getTotalAmount() + ",");
            out.print("\"totalAmount\":" + b.getTotalAmount() + ",");
            out.print("\"amountPaid\":" + b.getAmountPaid() + ",");
            out.print("\"balance\":" + b.getBalance() + ",");
            out.print("\"status\":\"" + esc(b.getStatus()) + "\",");
            out.print("\"createdDate\":\"" +
                    esc(b.getCreatedAt() == null ? "" : b.getCreatedAt().toString()) + "\"");
            out.print("}");
        } catch (NumberFormatException e) {
            sendError(response, "Invalid bill ID.");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Unable to load bill.");
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().print("{\"error\":\"" + esc(message) + "\"}");
    }

    private String esc(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
