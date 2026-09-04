package com.dentalclinic.controller;

import com.dentalclinic.dao.BillDAO;
import com.dentalclinic.model.Bill;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/UpdateBillServlet")
public class UpdateBillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        billDAO = new BillDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            int billId = Integer.parseInt(request.getParameter("billId"));
            double charge = Double.parseDouble(request.getParameter("treatmentCharge"));
            double discount = Double.parseDouble(request.getParameter("discount"));
            double paid = Double.parseDouble(request.getParameter("amountPaid"));

            if (charge < 0 || discount < 0 || paid < 0) {
                send(response, false, "Amounts cannot be negative.");
                return;
            }
            if (discount > charge) {
                send(response, false, "Discount cannot be greater than treatment charge.");
                return;
            }

            Bill existing = billDAO.getBillById(billId);
            if (existing == null) {
                send(response, false, "Bill not found.");
                return;
            }

            double total = Math.max(charge - discount, 0);
            if (paid > total) {
                send(response, false, "Amount paid cannot be greater than the total.");
                return;
            }

            double balance = total - paid;
            String status;
            if (paid <= 0) status = "Unpaid";
            else if (paid >= total) status = "Paid";
            else status = "Partial";

            existing.setTreatmentCharge(charge);
            existing.setDiscount(discount);
            existing.setTotalAmount(total);
            existing.setAmountPaid(paid);
            existing.setBalance(balance);
            existing.setStatus(status);

            boolean updated = billDAO.updateBill(existing);
            send(response, updated, updated ? "Bill updated successfully." : "Bill could not be updated.");

        } catch (NumberFormatException e) {
            send(response, false, "Please enter valid bill values.");
        } catch (Exception e) {
            e.printStackTrace();
            send(response, false, "Server error while updating the bill.");
        }
    }

    private void send(HttpServletResponse response, boolean success, String message) throws IOException {
        PrintWriter out = response.getWriter();
        out.print("{\"success\":" + success + ",\"message\":\"" +
                message.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
    }
}
