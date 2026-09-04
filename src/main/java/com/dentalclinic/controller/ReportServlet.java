package com.dentalclinic.controller;

import com.dentalclinic.dao.ReportDAO;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ReportServlet", urlPatterns = {"/api/reports"})
public class ReportServlet extends HttpServlet {

    private ReportDAO reportDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        reportDAO = new ReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Map<String, Object> reportData = reportDAO.getReportData();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(reportData));

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            Map<String, String> error = Map.of(
                "error", "Unable to load report data",
                "message", e.getMessage() == null ? "Unknown error" : e.getMessage()
            );

            response.getWriter().write(gson.toJson(error));
        }
    }
}
