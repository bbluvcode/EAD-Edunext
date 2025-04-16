<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết hóa đơn</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/exportBill.css">
    </head>
    <body>
        <div class="container py-4 w-50">        
            <div class="mb-4 bg-light p-3 rounded shadow-sm">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <c:choose>
                        <c:when test="${not empty sessionScope.patient}">
                            <a href="AppointmentServlet?action=GetAppsByPatient">
                                <button class="border-el-btn">
                                    ← Quay lại
                                    <span class="b1"></span>
                                    <span class="b2"></span>
                                    <span class="b3"></span>
                                    <span class="b4"></span>
                                </button>
                            </a>
                        </c:when>
                        <c:when test="${not empty sessionScope.doctor}">
                            <a href="DoctorServlet">
                                <button class="border-el-btn">
                                    ← Quay lại
                                    <span class="b1"></span>
                                    <span class="b2"></span>
                                    <span class="b3"></span>
                                    <span class="b4"></span>
                                </button>
                            </a>
                        </c:when>
                    </c:choose>                 
                    <form action="AppointmentServlet" method="post">
                        <input type="hidden" name="billId" value="${bill.billID}" />
                        <button type="submit" class="buttonExport" name="action" value="ExportPDF">
                            <span class="button__text">🖨️ In đơn thuốc</span>
                            <span class="button__icon">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 35 35" id="bdd05811-e15d-428c-bb53-8661459f9307" data-name="Layer 2" class="svg">
                                <path d="M17.5,22.131a1.249,1.249,0,0,1-1.25-1.25V2.187a1.25,1.25,0,0,1,2.5,0V20.881A1.25,1.25,0,0,1,17.5,22.131Z"></path>
                                <path d="M17.5,22.693a3.189,3.189,0,0,1-2.262-.936L8.487,15.006a1.249,1.249,0,0,1,1.767-1.767l6.751,6.751a.7.7,0,0,0,.99,0l6.751-6.751a1.25,1.25,0,0,1,1.768,1.767l-6.752,6.751A3.191,3.191,0,0,1,17.5,22.693Z"></path>
                                <path d="M31.436,34.063H3.564A3.318,3.318,0,0,1,.25,30.749V22.011a1.25,1.25,0,0,1,2.5,0v8.738a.815.815,0,0,0,.814.814H31.436a.815.815,0,0,0,.814-.814V22.011a1.25,1.25,0,1,1,2.5,0v8.738A3.318,3.318,0,0,1,31.436,34.063Z"></path>
                                </svg>
                            </span>
                        </button>
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
                    <a href="AppointmentServlet?action=GetPatient&patientID=${bill.appointmentID.patientID.patientID}">
                        <button class="continue-application">
                            <div>
                                <div class="pencil"></div>
                                <div class="folder">
                                    <div class="top">
                                        <svg viewBox="0 0 24 27">
                                        <path d="M1,0 L23,0 C23.5522847,-1.01453063e-16 24,0.44771525 24,1 L24,8.17157288 C24,8.70200585 23.7892863,9.21071368 23.4142136,9.58578644 L20.5857864,12.4142136 C20.2107137,12.7892863 20,13.2979941 20,13.8284271 L20,26 C20,26.5522847 19.5522847,27 19,27 L1,27 C0.44771525,27 6.76353751e-17,26.5522847 0,26 L0,1 C-6.76353751e-17,0.44771525 0.44771525,1.01453063e-16 1,0 Z">                                            
                                        </path>
                                        </svg>
                                    </div>
                                    <div class="paper"></div>
                                </div>
                            </div>
                            Tạo cuộc hẹn tới
                        </button>
                    </a>
                </div>
            </div>         
        </div>
    </body>
</html>
