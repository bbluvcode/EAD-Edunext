/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import entities.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
                    String patientID = request.getParameter("patientID");
                    Patients abc = null;
                    if (patientID != null) {
                        abc = sb.getPatient(Integer.parseInt(patientID));
                    } else {

                        abc = (Patients) session.getAttribute("patient");
                    }
                    Doctors doctocLogin = (Doctors) session.getAttribute("doctor");
                    request.setAttribute("doctorLogin", doctocLogin);
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

                    Patients loggedInPatient = (Patients) session.getAttribute("patient");
                    if (loggedInPatient != null) {
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("DoctorServlet");
                    }
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
                        Appointments app = sb.getAppointment(re.getAppointmentID().getAppointmentID());
                        app.setStatus("Completed");
                        sb.updateBooking(app);
                        response.sendRedirect("DoctorServlet");
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
                    List<PrescriptionDTO> presList = sb.getPrescriptionsByApp(bill.getAppointmentID().getAppointmentID());
                    try {
                        exportPrescriptionToPDF(bill, presList, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                case "GetAppsByPatient":
                    Patients pat = (Patients) session.getAttribute("patient");
                    request.setAttribute("aList", sb.getAppsByPatients(pat.getPatientID()));
                    request.setAttribute("bill", sb.getOneBill(pat.getPatientID()));
                    request.getRequestDispatcher("appHistory.jsp").forward(request, response);
                    break;
                case "Cancel":
                    int cancelID = Integer.parseInt(request.getParameter("appointmentId"));
                    Appointments cancelApp = sb.getAppointment(cancelID);
                    Patients cancelPat = sb.getPatient(cancelApp.getPatientID().getPatientID());
                    
                    Date dateApp = cancelApp.getAppointmentDate();

                    LocalDate today = LocalDate.now();
                    LocalDate appDate = dateApp.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    if (appDate.isAfter(today)) {
                        sb.cancel(cancelApp);
                        request.setAttribute("success", "Đã hủy lịch hẹn thành công");
                    } else {
                        request.setAttribute("error", "Chỉ được hủy lịch hẹn trước ngày hẹn");
                    }

                    request.setAttribute("aList", sb.getAppsByPatients(cancelPat.getPatientID()));
                    request.setAttribute("bill", sb.getOneBill(cancelPat.getPatientID()));
                    request.getRequestDispatcher("appHistory.jsp").forward(request, response);
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    private void exportPrescriptionToPDF(Bills bill, List<PrescriptionDTO> presList, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=DonThuoc_" + LocalDateTime.now().toString() + ".pdf");

        try {
            // Load font hỗ trợ tiếng Việt
            String fontPath = getServletContext().getRealPath("/WEB-INF/fonts/arial.ttf");
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);
            Font boldFont = new Font(bf, 12, Font.BOLD);
            Font titleFont = new Font(bf, 18, Font.BOLD, Color.BLUE);

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Tiêu đề
            Paragraph title = new Paragraph("ĐƠN THUỐC KHÁM BỆNH", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // khoảng trắng

            PdfPTable timeInfo = new PdfPTable(2);
            timeInfo.setWidthPercentage(100);
            timeInfo.setSpacingBefore(5f);
            timeInfo.setSpacingAfter(10f);
            timeInfo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            timeInfo.addCell(new Phrase("Ngày khám: " + bill.getAppointmentID().formatDate(), font));
            timeInfo.addCell(new Phrase("Giờ khám: " + bill.getAppointmentID().formatTime(), font));
            document.add(timeInfo);

            // Thông tin bệnh nhân
            document.add(new Paragraph("THÔNG TIN BỆNH NHÂN", boldFont));
            PdfPTable patientInfo = new PdfPTable(2);
            patientInfo.setWidthPercentage(100);
            patientInfo.setSpacingBefore(5f);
            patientInfo.setSpacingAfter(10f);
            patientInfo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            patientInfo.addCell(new Phrase("Họ tên: " + bill.getAppointmentID().getPatientID().getFullName(), font));
            patientInfo.addCell(new Phrase("Giới tính: " + bill.getAppointmentID().getPatientID().getGender(), font));
            patientInfo.addCell(new Phrase("Ngày sinh: " + bill.getAppointmentID().getPatientID().formatDOB(), font));
            patientInfo.addCell(new Phrase("SĐT: " + bill.getAppointmentID().getPatientID().getPhone(), font));
            patientInfo.addCell(new Phrase("Địa chỉ: " + bill.getAppointmentID().getPatientID().getAddress(), font));
            patientInfo.addCell("");
            document.add(patientInfo);

            // Thông tin bác sĩ khám (chia làm 2 cột như trong file JSP)
            document.add(new Paragraph("THÔNG TIN BÁC SĨ KHÁM", boldFont));
            PdfPTable doctorInfo = new PdfPTable(2);
            doctorInfo.setWidthPercentage(100);
            doctorInfo.setSpacingBefore(5f);
            doctorInfo.setSpacingAfter(10f);
            doctorInfo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            doctorInfo.addCell(new Phrase("Họ tên: " + bill.getAppointmentID().getDoctorID().getFullName(), font));
            doctorInfo.addCell(new Phrase("Chuyên khoa: " + bill.getAppointmentID().getDoctorID().getSpecialization(), font));

            document.add(doctorInfo);

            // Tiêu đề bảng thuốc
            document.add(new Paragraph("💊 DANH SÁCH THUỐC", boldFont));

            // Bảng thuốc
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 2, 2, 2, 2});
            table.setSpacingBefore(10);

            String[] headers = {"STT", "Tên thuốc", "Liều dùng", "Đơn vị", "Số lượng", "Giá", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int stt = 1;
            BigDecimal total = BigDecimal.ZERO;
            for (PrescriptionDTO detail : presList) {
                BigDecimal itemTotal = detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                total = total.add(itemTotal);

                PdfPCell sttCell = new PdfPCell(new Phrase(String.valueOf(stt++), font));
                PdfPCell nameCell = new PdfPCell(new Phrase(detail.getMedicineName(), font));
                PdfPCell dosageCell = new PdfPCell(new Phrase(detail.getDosage(), font));
                PdfPCell unitCell = new PdfPCell(new Phrase(detail.getUnit(), font));
                PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(detail.getQuantity()), font));
                PdfPCell priceCell = new PdfPCell(new Phrase(String.format("%.2f", detail.getPrice()), font));
                PdfPCell totalCell = new PdfPCell(new Phrase(String.format("%.2f", itemTotal), font));

                PdfPCell[] row = {sttCell, nameCell, dosageCell, unitCell, qtyCell, priceCell, totalCell};
                for (PdfPCell c : row) {
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c);
                }
            }

            PdfPCell totalCell = new PdfPCell(new Phrase("Tổng tiền", boldFont));
            totalCell.setColspan(6);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.format("%.2f$", total), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.RED)));
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(totalAmountCell);

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
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
