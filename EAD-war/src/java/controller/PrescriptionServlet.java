/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.MedicalSBLocal;
import entities.MedicalRecords;
import entities.Medicines;
import entities.Prescriptions;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.List;
// import javax.ejb.EJB;

/**
 *
 * @author Admin
 */
@WebServlet(name = "PrescriptionServlet", urlPatterns = { "/PrescriptionServlet" })
public class PrescriptionServlet extends HttpServlet {

    @EJB
    private MedicalSBLocal medicalSB;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PrescriptionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PrescriptionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("getMedicines".equals(action)) {
            try {
                // Fetch the list of medicines from the database
                List<Medicines> medicines = medicalSB.getAllMedicines();

                // Convert the list to JSON
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.write(new Gson().toJson(medicines));
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
            }
        } else if ("delete".equals(action)) {
            try {
                String presId = request.getParameter("prescriptionId");
                int prescriptionId = Integer.parseInt(presId);
                medicalSB.deletePrescription(prescriptionId);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
            }
        } else {
            processRequest(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("update".equals(action)) {
                int prescriptionId = Integer.parseInt(request.getParameter("prescriptionId"));
                int medicineId = Integer.parseInt(request.getParameter("medicineId"));
                String dosage = request.getParameter("dosage");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int duration = Integer.parseInt(request.getParameter("duration"));

                Prescriptions prescription = medicalSB.getPrescriptionById(prescriptionId);
                Medicines medicine = new Medicines();
                medicine.setMedicineID(medicineId);

                prescription.setMedicineID(medicine);
                prescription.setDosage(dosage);
                prescription.setQuantity(quantity);
                prescription.setDuration(duration);

                medicalSB.updatePrescription(prescription);
                response.sendRedirect("MedicalServlet?appointmentId=" + request.getParameter("appointmentId"));
            } else {
                System.out.println("Creating new prescription");
                System.out.println("appointmentId" + request.getParameter("appointmentId"));
                int recordId = Integer.parseInt(request.getParameter("recordId"));
                int medicineId = Integer.parseInt(request.getParameter("medicineId"));
                String dosage = request.getParameter("dosage");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int duration = Integer.parseInt(request.getParameter("duration"));

                MedicalRecords record = new MedicalRecords();
                record.setRecordID(recordId);

                Medicines medicine = new Medicines();
                medicine.setMedicineID(medicineId);

                Prescriptions prescription = new Prescriptions(quantity, dosage, duration, record, medicine);
                medicalSB.addPrescription(prescription);
                medicalSB.updatePrescription(prescription);
                response.sendRedirect("MedicalServlet?appointmentId=" + request.getParameter("appointmentId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
