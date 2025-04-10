/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.*;
import entities.*;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class AppointmentServlet extends HttpServlet {

    @EJB
    AppointmentSBLocal sb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            if (action == null) {
                request.setAttribute("appList", sb.getAppointments());
                request.getRequestDispatcher("indexAdmin.jsp").forward(request, response);
            } else {
                switch (action) {
                    case "GetPatient":
                        request.setAttribute("p", sb.getPatient(1));
                        request.getRequestDispatcher("booking.jsp").forward(request, response);
                        break;
                    case "Booking":
                        String selectedDate = request.getParameter("selectedDate");
                        String selectedTime = request.getParameter("selectedTime");
                        String notes = request.getParameter("notes");
                        String doctorIdStr = request.getParameter("doctorId");
                        String patientIdStr = request.getParameter("patientId");
                        String department = request.getParameter("department");

                        if (department == null || department.isEmpty() || department.equals("")) {
                            request.setAttribute("errDepart", "Vui lòng chọn ngành khám");
                            request.setAttribute("p", sb.getPatient(1));
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (doctorIdStr == null || doctorIdStr.isEmpty() || doctorIdStr.equals("0")) {
                            request.setAttribute("errDoctor", "Vui lòng chọn bác sĩ");
                            request.setAttribute("p", sb.getPatient(1));
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (selectedDate == null || selectedDate.isEmpty()) {
                            request.setAttribute("errDate", "Vui lòng chọn ngày hẹn");
                            request.setAttribute("p", sb.getPatient(1));
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (selectedTime == null || selectedTime.isEmpty()) {
                            request.setAttribute("errTime", "Vui lòng chọn giờ hẹn");
                            request.setAttribute("p", sb.getPatient(1));
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        int doctorId = Integer.parseInt(doctorIdStr);
                        int patientId = Integer.parseInt(patientIdStr);

                        Date appointmentDate = null;
                        try {
                            appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedDate);
                        } catch (ParseException ex) {
                            Logger.getLogger(AppointmentServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Date appointmentTime = null;
                        try {
                            appointmentTime = new SimpleDateFormat("HH:mm").parse(selectedTime);
                        } catch (ParseException ex) {
                            Logger.getLogger(AppointmentServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Doctors db = sb.getDoctor(doctorId);
                        Patients pb = sb.getPatient(patientId);
                        Appointments appb = new Appointments(appointmentDate, appointmentTime, "Scheduled", notes, db, pb);
                        sb.booking(appb);
                        response.sendRedirect("AppointmentServlet");
                        break;
                    case "AppointmentByDoctor":

                        break;

                    case "":

                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
