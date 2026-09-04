package com.dentalclinic.controller;

import com.dentalclinic.dao.BillDAO;
import com.dentalclinic.model.Bill;
import com.dentalclinic.util.RoleAccess;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@WebServlet("/PrintBillServlet")
public class PrintBillServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Only Admin and Cashier may print bills.
        if (!RoleAccess.allowed(request, "ADMIN", "CASHIER")) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only Admin and Cashier users can print bills."
            );
            return;
        }

        String value = request.getParameter("id");
        if (value == null || value.trim().isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bill ID is required."
            );
            return;
        }

        final int billId;
        try {
            billId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid bill ID."
            );
            return;
        }

        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Bill not found."
            );
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Bill #" + bill.getBillId() + " - Dental Clinic</title>");
        out.println("<style>");
        out.println("*{box-sizing:border-box}body{margin:0;background:#f4f6f9;font-family:Arial,Helvetica,sans-serif;color:#172033}");
        out.println(".print-page{width:800px;max-width:calc(100% - 30px);margin:30px auto;background:#fff;padding:40px;border-radius:12px;box-shadow:0 8px 25px rgba(0,0,0,.10)}");
        out.println(".clinic{text-align:center;border-bottom:2px solid #1565c0;padding-bottom:20px;margin-bottom:25px}.clinic h1{margin:0;color:#1565c0;font-size:30px}.clinic p{margin:6px 0;color:#64748b}");
        out.println(".meta{display:grid;grid-template-columns:1fr 1fr;gap:10px 30px;margin-bottom:25px}.meta div{padding:8px 0}.label{color:#64748b;font-size:13px}.value{font-weight:700;margin-top:3px}");
        out.println("table{width:100%;border-collapse:collapse;margin-top:15px}th,td{padding:12px;border-bottom:1px solid #e5e7eb;text-align:left}th{background:#1565c0;color:#fff}td.amount{text-align:right}th.amount{text-align:right}");
        out.println(".totals{margin-left:auto;width:320px;margin-top:20px}.totals div{display:flex;justify-content:space-between;padding:8px 0}.totals .grand{font-size:18px;font-weight:700;border-top:2px solid #1565c0;margin-top:5px;padding-top:12px}.paid{color:#198754}.balance{color:#dc3545}");
        out.println(".status{display:inline-block;padding:7px 14px;border-radius:20px;background:#e8f5e9;color:#198754;font-weight:700}.footer{text-align:center;margin-top:35px;color:#64748b;font-size:13px}");
        out.println(".no-print{text-align:center;margin:20px auto}.no-print button{border:0;background:#1565c0;color:#fff;padding:12px 22px;border-radius:7px;font-weight:700;cursor:pointer}.no-print button:hover{background:#0d47a1}");
        out.println("@media print{body{background:#fff}.print-page{width:100%;max-width:none;margin:0;padding:10px;box-shadow:none;border-radius:0}.no-print{display:none!important}@page{size:A4;margin:12mm}}");
        out.println("</style></head><body>");
        out.println("<div class='no-print'><button onclick='window.print()'>🖨 Print Bill</button></div>");
        out.println("<main class='print-page'>");
        out.println("<section class='clinic'><h1>🦷 Dental Clinic</h1><p>Official Payment Receipt</p></section>");
        out.println("<section class='meta'>");
        out.println(meta("Bill ID", String.valueOf(bill.getBillId())));
        out.println(meta("Appointment ID", String.valueOf(bill.getAppointmentId())));
        out.println(meta("Patient", safe(bill.getPatientName())));
        out.println(meta("Dentist", safe(bill.getDentistName())));
        out.println(meta("Date", bill.getCreatedAt() == null ? "-" : safe(bill.getCreatedAt().toString())));
        out.println(meta("Status", safe(bill.getStatus())));
        out.println("</section>");
        out.println("<table><thead><tr><th>Description</th><th class='amount'>Amount</th></tr></thead><tbody>");
        out.println("<tr><td>Treatment Charge</td><td class='amount'>Rs. " + money(bill.getTreatmentCharge()) + "</td></tr>");
        out.println("<tr><td>Discount</td><td class='amount'>Rs. " + money(bill.getDiscount()) + "</td></tr>");
        out.println("</tbody></table>");
        out.println("<section class='totals'>");
        out.println("<div><span>Total</span><strong>Rs. " + money(bill.getTotalAmount()) + "</strong></div>");
        out.println("<div class='paid'><span>Amount Paid</span><strong>Rs. " + money(bill.getAmountPaid()) + "</strong></div>");
        out.println("<div class='balance'><span>Balance</span><strong>Rs. " + money(bill.getBalance()) + "</strong></div>");
        out.println("<div class='grand'><span>Payment Status</span><span class='status'>" + safe(bill.getStatus()) + "</span></div>");
        out.println("</section>");
        out.println("<div class='footer'>Thank you for visiting our Dental Clinic.</div>");
        out.println("</main></body></html>");
    }

    private String meta(String label, String value) {
        return "<div><div class='label'>" + label + "</div><div class='value'>" + value + "</div></div>";
    }

    private String money(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    private String safe(String value) {
        if (value == null) return "-";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
