/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.*;
import entities.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
            HttpSession session = request.getSession();
            String action = request.getParameter("action");
            if (action == null) {
                //Danh sách lịch hẹn khám
                request.setAttribute("appList", sb.getAppointments());
                request.getRequestDispatcher("indexAppointment.jsp").forward(request, response);
            } else {
                switch (action) {
                    //Lấy thông bệnh nhân đăng kí lịch hẹn khám      
                    case "GetPatient":
                        Patients abc = (Patients) session.getAttribute("patient");
                        request.setAttribute("p", abc);
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
                        response.sendRedirect("index.jsp");
                        break;

                    //Lấy danh sách thuốc 
                    case "GetMedicine":
                        int appointmentIDFromDoc = Integer.parseInt(request.getParameter("appointmentId"));
                        request.setAttribute("p", sb.getOneMedicalRecords(sb.getRecordID(appointmentIDFromDoc)));
                        request.setAttribute("history", sb.getMedicinesByRecord(appointmentIDFromDoc));
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

                            response.sendRedirect("DoctorServlet");
                            //response.sendRedirect("AppointmentServlet?action=GetBill&appointmentId=" + re.getAppointmentID().getAppointmentID());
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
                    // Hóa đơn
                    case "GetBill":
                        int appointmentIDFromDocBill = Integer.parseInt(request.getParameter("appointmentId"));
                        request.setAttribute("bill", sb.getOneBill(appointmentIDFromDocBill));
                        request.setAttribute("presList", sb.getPrescriptionsByApp(appointmentIDFromDocBill));
                        request.getRequestDispatcher("oneBill.jsp").forward(request, response);
                        break;
                    case "ExportPDF":
                        int bid = Integer.parseInt(request.getParameter("billId"));
                        Bills bill = sb.getBillDetail(bid);
                        String html = generateHTMLFromBill(bill, request.getServletContext());

                        try {
                            // Xuất file PDF từ HTML
//                            String outputPath = request.getServletContext().getRealPath("/") + "bill.pdf";
//                            OutputStream outputStream = new FileOutputStream(outputPath);
//
//                            ITextRenderer renderer = new ITextRenderer();
//                            renderer.setDocumentFromString(html);
//                            renderer.layout();
//                            renderer.createPDF(outputStream);
//
//                            outputStream.close();

                            // Redirect hoặc mở file PDF
                            response.sendRedirect("DoctorServlet");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }
    }

    private String generateHTMLFromBill(Bills bill, ServletContext context) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
        html.append("<style>");
        html.append("body { font-family: 'Arial'; }");
        html.append(".title { text-align: center; color: #0d6efd; margin-bottom: 20px; }");
        html.append(".section-title { font-weight: bold; color: #198754; margin-top: 20px; }");
        html.append(".row-info { display: flex; justify-content: space-between; margin-bottom: 10px; }");
        html.append(".signature { text-align: right; margin-top: 50px; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<div class='container py-4 w-75'>");

        // Tiêu đề
        html.append("<h2 class='title'>ĐƠN THUỐC KHÁM BỆNH</h2>");

        // Dòng thông tin chia 2 cột
        html.append("<div class='row'>");
        html.append("  <div class='col-6'>");
        html.append("    <p><strong>👤 Họ tên bệnh nhân:</strong> ").append(bill.getAppointmentID().getPatientID().getFullName()).append("</p>");
        html.append("    <p><strong>🎂 Ngày sinh:</strong> ").append(bill.getAppointmentID().getPatientID().formatDOB()).append("</p>");
        html.append("    <p><strong>📞 Số điện thoại:</strong> ").append(bill.getAppointmentID().getPatientID().getPhone()).append("</p>");
        html.append("    <p><strong>🏠 Địa chỉ:</strong> ").append(bill.getAppointmentID().getPatientID().getAddress()).append("</p>");
        html.append("  </div>");
        html.append("  <div class='col-6'>");
        html.append("    <p><strong>🧑‍⚕️ Bác sĩ:</strong> ").append(bill.getAppointmentID().getDoctorID().getFullName()).append("</p>");
        html.append("    <p><strong>📚 Chuyên khoa:</strong> ").append(bill.getAppointmentID().getDoctorID().getSpecialization()).append("</p>");
        html.append("    <p><strong>📅 Ngày khám:</strong> ").append(bill.getAppointmentID().getAppointmentDate()).append("</p>");
        html.append("    <p><strong>🕘 Giờ khám:</strong> ").append(bill.getAppointmentID().getAppointmentTime()).append("</p>");
        html.append("  </div>");
        html.append("</div>");

        // Danh sách thuốc
        html.append("<h5 class='text-danger mt-4'>💊 Danh sách thuốc</h5>");
        html.append("<table class='table table-bordered'><thead class='table-light'><tr>");
        html.append("<th>STT</th><th>Tên thuốc</th><th>Liều dùng</th><th>Đơn vị</th><th>Số lượng</th><th>Giá</th><th>Thành tiền</th>");
        html.append("</tr></thead><tbody>");

        DecimalFormat df = new DecimalFormat("#,##0.00");
        BigDecimal total = BigDecimal.ZERO;
        int stt = 1;

        for (Prescriptions p : bill.getAppointmentID().getMedicalRecordsList().get(0).getPrescriptionsList()) {
            BigDecimal quantity = BigDecimal.valueOf(p.getQuantity());
            BigDecimal price = p.getMedicineID().getPrice();
            BigDecimal itemTotal = quantity.multiply(price);
            total = total.add(itemTotal);

            html.append("<tr>");
            html.append("<td>").append(stt++).append("</td>");
            html.append("<td>").append(p.getMedicineID().getMedicineName()).append("</td>");
            html.append("<td>").append(p.getDosage()).append("</td>");
            html.append("<td>").append(p.getMedicineID().getUnit()).append("</td>");
            html.append("<td>").append(p.getQuantity()).append("</td>");
            html.append("<td>").append(df.format(price)).append("</td>");
            html.append("<td>").append(df.format(itemTotal)).append("</td>");
            html.append("</tr>");
        }
        html.append("</tbody><tfoot><tr><td colspan='6' class='text-end fw-bold'>Tổng tiền</td>")
                .append("<td class='fw-bold text-danger'>").append(df.format(total)).append(" $</td></tr></tfoot></table>");

        // Chữ ký
        html.append("<div class='signature'>");
        html.append("<p><strong>Ngày lập: </strong>").append(java.time.LocalDate.now()).append("</p>");
        html.append("<p><strong>Bác sĩ ký tên</strong></p>");
        html.append("<p class='mt-5'>").append(bill.getAppointmentID().getDoctorID().getFullName()).append("</p>");
        html.append("</div>");

        html.append("</div></body></html>");
        return html.toString();
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
