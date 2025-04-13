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
                //Danh sách lịch hẹn khám
                request.setAttribute("appList", sb.getAppointments());
                request.getRequestDispatcher("indexAppointment.jsp").forward(request, response);
            } else {
                switch (action) {
                    //Lấy thông bệnh nhân đăng kí lịch hẹn khám      //Sửa sau
                    case "GetPatient":
                        request.setAttribute("p", sb.getPatient(7));
                        request.getRequestDispatcher("booking.jsp").forward(request, response);
                        break;
                    //Tạo lịch hẹn khám
                    case "Booking":
                        String selectedDate = request.getParameter("selectedDate");
                        String selectedTime = request.getParameter("selectedTime");
                        String notes = request.getParameter("notes");
                        String doctorIdStr = request.getParameter("doctorId");
                        String patientIdStr = request.getParameter("patientId");
                        String department = request.getParameter("department");

                        int patientId = Integer.parseInt(patientIdStr);
                        Patients pb = sb.getPatient(patientId);

                        if (department == null || department.isEmpty() || department.equals("")) {
                            request.setAttribute("errDepart", "Vui lòng chọn ngành khám");
                            request.setAttribute("p", pb);
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (doctorIdStr == null || doctorIdStr.isEmpty() || doctorIdStr.equals("0")) {
                            request.setAttribute("errDoctor", "Vui lòng chọn bác sĩ");
                            request.setAttribute("p", pb);
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (selectedDate == null || selectedDate.isEmpty()) {
                            request.setAttribute("errDate", "Vui lòng chọn ngày hẹn");
                            request.setAttribute("p", pb);
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        if (selectedTime == null || selectedTime.isEmpty()) {
                            request.setAttribute("errTime", "Vui lòng chọn giờ hẹn");
                            request.setAttribute("p", pb);
                            request.getRequestDispatcher("booking.jsp").forward(request, response);
                            break;
                        }

                        int doctorId = Integer.parseInt(doctorIdStr);
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
                        Appointments appb = new Appointments(appointmentDate, appointmentTime, "Da dat lich kham", notes, db, pb);
                        sb.booking(appb);
                        response.sendRedirect("AppointmentServlet");
                        break;

                    //Lấy danh sách thuốc   //Sửa sau
                    case "GetMedicine":
                        request.setAttribute("p", sb.getOneMedicalRecords(7));
                        request.setAttribute("medicines", sb.getMedicines());
                        request.getRequestDispatcher("createPrescription.jsp").forward(request, response);
                        break;
                    //Tạo đơn thuốc
                    case "CreatePrescription":
                        int recordID = Integer.parseInt(request.getParameter("recordID"));
                        MedicalRecords re = sb.getOneMedicalRecords(recordID);
                        String[] medicineIDs = request.getParameterValues("medicineName[]");
                        String[] dosages = request.getParameterValues("dosage[]");
                        String[] quantity = request.getParameterValues("quantity[]");
                        String[] duration = request.getParameterValues("duration[]");

                        if (medicineIDs == null || dosages == null || quantity == null || duration == null) {
                            request.setAttribute("error", "Vui lòng thêm ít nhất một loại thuốc và nhập đầy đủ thông tin");
                            request.setAttribute("p", re);
                            request.setAttribute("medicines", sb.getMedicines());
                            request.getRequestDispatcher("createPrescription.jsp").forward(request, response);
                            return;
                        }
                        try {
                            boolean hasError = false;
                            String errorMessage = "";

                            if (medicineIDs == null || dosages == null || quantity == null || duration == null) {
                                hasError = true;
                                errorMessage = "Vui lòng điền đầy đủ thông tin đơn thuốc.";
                            } else {
                                for (int i = 0; i < medicineIDs.length; i++) {
                                    if (medicineIDs[i] == null || medicineIDs[i].isEmpty()
                                            || dosages[i] == null || dosages[i].trim().isEmpty()
                                            || quantity[i] == null || quantity[i].isEmpty()
                                            || duration[i] == null || duration[i].isEmpty()) {
                                        hasError = true;
                                        errorMessage = "Vui lòng không để trống bất kỳ trường nào trong đơn thuốc.";
                                        break;
                                    }
                                    try {
                                        Integer.parseInt(quantity[i]);
                                        Integer.parseInt(duration[i]);
                                    } catch (NumberFormatException e) {
                                        hasError = true;
                                        errorMessage = "Số lượng và số ngày phải là số hợp lệ.";
                                        break;
                                    }
                                }
                            }

                            if (hasError) {
                                request.setAttribute("error", errorMessage);
                                request.setAttribute("medicineIDs", medicineIDs);
                                request.setAttribute("dosages", dosages);
                                request.setAttribute("quantities", quantity);
                                request.setAttribute("durations", duration);
                                request.setAttribute("p", re);
                                request.setAttribute("medicines", sb.getMedicines());
                                request.getRequestDispatcher("createPrescription.jsp").forward(request, response);
                                return;
                            }

                            for (int i = 0; i < medicineIDs.length; i++) {
                                Medicines m = sb.getOneMedicines(Integer.parseInt(medicineIDs[i]));
                                Prescriptions p = new Prescriptions(Integer.parseInt(quantity[i]), dosages[i], Integer.parseInt(duration[i]), re, m);
                                sb.addPrescriptions(p);
                            }
                            response.sendRedirect("AppointmentServlet");

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình tạo đơn thuốc.");
                            request.getRequestDispatcher("createPrescription.jsp").forward(request, response);
                        }

                        break;
                    // Danh sách đơn thuốc
                    case "GetPrescriptionsList":
                        request.setAttribute("pList", sb.getPrescriptions());
                        request.getRequestDispatcher("indexPrescription.jsp").forward(request, response);
                        break;
                    // Danh sách hóa đơn
                    case "GetBillsList":
                        request.setAttribute("bList", sb.getBills());
                        request.getRequestDispatcher("indexBill.jsp").forward(request, response);
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
