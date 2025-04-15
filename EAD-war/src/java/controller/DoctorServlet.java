/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.DoctorSBLocal;
import entities.*;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;

/**
 *
 * @author ACER
 */
public class DoctorServlet extends HttpServlet {

    @EJB
    DoctorSBLocal sb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String reStr = request.getParameter("recordID");
            int recordIDFromRecord = 0;
            if (reStr != null) {
                recordIDFromRecord = Integer.parseInt(reStr);
            }
            String action = request.getParameter("action");
            if (null == action) {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("doctor") == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                Doctors d = (Doctors) session.getAttribute("doctor");
                if (d == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (Boolean.TRUE.equals(d.getRole())) {
                    // Admin → hiển thị tất cả doctors + appointment count
                    List<Doctors> allDoctors = sb.getDoctorsWithoutAdmin();
                    Map<Integer, Integer> appointmentCounts = new HashMap<>();

                    for (Doctors doc : allDoctors) {
                        int count = sb.getAppointmentsByDoctorId(doc.getDoctorID()).size();
                        appointmentCounts.put(doc.getDoctorID(), count);
                    }

                    request.setAttribute("doctors", allDoctors);
                    request.setAttribute("appointmentCounts", appointmentCounts);
                    request.getRequestDispatcher("indexDoctor.jsp").forward(request, response);

                } else {
                    // Doctor thường → hiển thị trang chi tiết cá nhân
                    List<Appointments> doctorAppointments = sb.getAppointmentsByDoctorId(d.getDoctorID());
                    request.setAttribute("appointments", doctorAppointments);
                    request.getRequestDispatcher("detailDoctor.jsp").forward(request, response);
                }
            } else {
                switch (action) {
                    case "detail":
                        try {
                            request.setAttribute("recordID", recordIDFromRecord);
                            int id = Integer.parseInt(request.getParameter("id"));
                            Doctors doctor = sb.getDoctor(id); // gọi EJB để lấy thông tin bác sĩ
                            List<Appointments> appointments = sb.getAppointmentsByDoctorId(id); // lấy danh sách cuộc hẹn
                            request.setAttribute("doctor", doctor); // gán vào request thay vì session
                            request.setAttribute("appointments", appointments);                        
                            request.getRequestDispatcher("detailDoctor.jsp").forward(request, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.sendRedirect("error.jsp"); // hoặc hiển thị thông báo lỗi
                        }
                        break;

                    case "create":
                        request.getRequestDispatcher("createDoctor.jsp").forward(request, response);
                        break;

                    case "createSubmit":
                        Doctors newDoc = new Doctors();
                        newDoc.setFullName(request.getParameter("fullName"));
                        newDoc.setEmail(request.getParameter("email"));
                        newDoc.setSpecialization(request.getParameter("specialization"));
                        newDoc.setPhone(request.getParameter("phone"));
                        newDoc.setPassword(request.getParameter("password")); // bạn có thể hash nếu cần
                        newDoc.setRole(false); // mặc định không phải admin
                        sb.createDoctor(newDoc);
                        response.sendRedirect("DoctorServlet");
                        break;

                    case "update":
                        int updateId = Integer.parseInt(request.getParameter("id"));
                        Doctors doctorToEdit = sb.getDoctor(updateId);
                        request.setAttribute("doctor", doctorToEdit);
                        request.getRequestDispatcher("updateDoctor.jsp").forward(request, response);
                        break;

                    case "updateSubmit":
                        int id = Integer.parseInt(request.getParameter("doctorID"));
                        Doctors docToUpdate = sb.getDoctor(id);
                        docToUpdate.setFullName(request.getParameter("fullName"));
                        docToUpdate.setEmail(request.getParameter("email"));
                        docToUpdate.setSpecialization(request.getParameter("specialization"));
                        docToUpdate.setPhone(request.getParameter("phone"));
                        sb.updateDoctor(docToUpdate);
                        response.sendRedirect("DoctorServlet");
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
