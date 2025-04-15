<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết hóa đơn</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container py-4 w-50">        
            <div class="mb-4 bg-light p-3 rounded shadow-sm">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <c:choose>
                        <c:when test="${not empty sessionScope.patient}">
                            <a href="AppointmentServlet?action=GetAppsByPatient" class="btn btn-danger">
                                ← Quay lại
                            </a>
                        </c:when>
                        <c:when test="${not empty sessionScope.doctor}">
                            <a href="DoctorServlet" class="btn btn-danger">
                                ← Quay lại
                            </a>
                        </c:when>
                    </c:choose>                 
                    <form action="AppointmentServlet" method="post">
                        <input type="hidden" name="billId" value="${bill.billID}" />
                        <button type="submit" class="btn btn-success" name="action" value="ExportPDF">🖨️ In đơn thuốc</button>
                    </form>
                </div>
                <h2 class="text-center mb-4 text-primary">Đơn thuốc khám bệnh</h2>
                <div class="row mb-3">
                    <div class="col-6"><strong>Ngày khám: </strong> ${bill.appointmentID.formatDate()}</div>
                    <div class="col-6"><strong>Giờ khám:</strong> ${bill.appointmentID.formatTime()}</div>
                </div>
                <div class="row mb-4">
                    <h5 class="text-success">👤 Thông tin bệnh nhân</h5>
                    <div class="row">
                        <div class="col-6"><strong>Họ tên:</strong> ${bill.appointmentID.patientID.fullName}</div>
                        <div class="col-6"><strong>Giới tính:</strong> ${bill.appointmentID.patientID.gender}</div>
                        <div class="col-6"><strong>Ngày sinh:</strong> ${bill.appointmentID.patientID.formatDOB()}</div>
                        <div class="col-6"><strong>SĐT:</strong> ${bill.appointmentID.patientID.phone}</div>
                        <div class="col-12"><strong>Địa chỉ:</strong> ${bill.appointmentID.patientID.address}</div>
                    </div>
                </div>
                <div class="row mb-4">
                    <h5 class="text-primary">🩺 Thông tin bác sĩ khám</h5>
                    <div class="row">
                        <div class="col-6"><strong>Họ tên:</strong> ${bill.appointmentID.doctorID.fullName}</div>
                        <div class="col-6"><strong>Chuyên khoa:</strong> ${bill.appointmentID.doctorID.specialization}</div>
                    </div>
                </div>
                <h5 class="text-danger mb-3">💊 Danh sách thuốc</h5>
                <table class="table table-bordered text-center mb-3">
                    <thead class="table-secondary">
                        <tr>
                            <th>STT</th>
                            <th>Tên thuốc</th>
                            <th>Liều dùng</th>
                            <th>Đơn vị</th>
                            <th>Số lượng</th>
                            <th>Giá</th>
                            <th>Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="total" value="0" />
                        <c:forEach var="detail" items="${presList}" varStatus="loop">
                            <c:set var="itemTotal" value="${detail.quantity * detail.price}" />
                            <c:set var="total" value="${total + itemTotal}" />
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${detail.medicineName}</td>
                                <td>${detail.dosage}</td>
                                <td>${detail.unit}</td>
                                <td>${detail.quantity}</td>
                                <td>${detail.price}</td>
                                <td>${itemTotal}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="6" class="text-end fw-bold">Tổng tiền</td>
                            <td class="fw-bold text-danger fs-5">${total}$</td>
                        </tr>
                    </tfoot>
                </table>
                <div class="d-flex justify-content-start mb-3">
                    <a href="AppointmentServlet?action=GetPatient&patientID=${bill.appointmentID.patientID.patientID}" class="btn btn-success">Tạo cuộc hẹn tới</a>
                </div>
            </div>         
        </div>
    </body>
</html>
