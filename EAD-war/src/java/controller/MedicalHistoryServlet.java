package controller;

import bean.MedicalSBLocal;
import entities.MedicalRecords;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MedicalHistoryServlet", urlPatterns = {"/MedicalHistoryServlet"})
public class MedicalHistoryServlet extends HttpServlet {

    @EJB
    private MedicalSBLocal medicalSB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patientIdStr = request.getParameter("patientId");
        String appointmentIdStr = request.getParameter("appointmentId");
        String searchQuery = request.getParameter("searchQuery"); // Retrieve search query

        if (patientIdStr != null) {
            try {
                int patientId = Integer.parseInt(patientIdStr);
                List<MedicalRecords> history;

                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    // Filter medical history based on the search query
                    history = medicalSB.searchMedicalHistoryByPatientId(patientId, searchQuery.trim());
                } else {
                    // Retrieve full medical history if no search query is provided
                    history = medicalSB.getMedicalHistoryByPatientId(patientId);
                }

                // Retrieve patient details
                String patientName = medicalSB.getPatientNameById(patientId);

                request.setAttribute("medicalHistory", history);
                request.setAttribute("appointmentId", appointmentIdStr);
                request.setAttribute("searchQuery", searchQuery); // Pass search query back to JSP
                request.setAttribute("patientName", patientName); // Pass patient name to JSP
                request.getRequestDispatcher("medicalHistory.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Patient ID is required.");
        }
    }
}