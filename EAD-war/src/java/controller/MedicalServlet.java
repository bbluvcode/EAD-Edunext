/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.MedicalSBLocal;
import entities.Appointments;
import entities.MedicalRecords;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 * @author Admin
 */
//@WebServlet(name = "MedicalServlet", urlPatterns = {"/MedicalServlet"})
public class MedicalServlet extends HttpServlet {

    @EJB
    private MedicalSBLocal medicalSB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr != null) {
            try {
                int appointmentId = Integer.parseInt(appointmentIdStr);
                MedicalRecords record = medicalSB.getMedicalRecordByAppointmentId(appointmentId);
                request.setAttribute("medicalRecord", record);
                request.getRequestDispatcher("medical.jsp").forward(request, response);
            } catch (Exception e) {
                request.getRequestDispatcher("medical.jsp").forward(request, response);
//                e.printStackTrace();
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointment ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Appointment ID is required.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                System.out.println("Create servlet: Hello Binh");
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                String symptoms = request.getParameter("symptoms");
                String diagnosis = request.getParameter("diagnosis");

                Appointments appointment = new Appointments(); // Replace with actual retrieval logic
                appointment.setAppointmentID(appointmentId);

                medicalSB.createMedicalRecord(appointment, symptoms, diagnosis);
                response.sendRedirect("MedicalServlet?appointmentId=" + appointmentId);

            } else if ("updateSymptoms".equals(action)) {
                int recordId = Integer.parseInt(request.getParameter("recordId"));
                String newSymptoms = request.getParameter("newSymptoms");

                medicalSB.updateSymptoms(recordId, newSymptoms);
                response.sendRedirect("MedicalServlet?appointmentId=" + recordId);

            } else if ("updateDiagnosis".equals(action)) {
                int recordId = Integer.parseInt(request.getParameter("recordId"));
                String newDiagnosis = request.getParameter("newDiagnosis");

                medicalSB.updateDiagnosis(recordId, newDiagnosis);
//                response.sendRedirect("MedicalServlet?appointmentId=" + request.getParameter("appointmentId"));
                response.sendRedirect("MedicalServlet?appointmentId=" + recordId);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for handling medical record operations.";
    }
}
