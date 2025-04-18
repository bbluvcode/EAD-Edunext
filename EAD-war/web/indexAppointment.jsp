<%-- 
    Document   : event
    Created on : Mar 21, 2025, 2:44:03 PM
    Author     : User
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4 w-100">
            <div class="d-flex align-items-center gap-2">
                <div class="mb-3 me-3">
                    <a href="AppointmentServlet?action=GetPatient" class="btn btn-success">Tạo cuộc hẹn khám</a>
                </div>
                <div class="mb-3 me-3">
                    <a href="AppointmentServlet?action=GetMedicine" class="btn btn-success">Tạo đơn thuốc</a>
                </div>
                <div class="mb-3 me-3">
                    <a href="AppointmentServlet?action=GetPrescriptionsList" class="btn btn-success">Danh sách đơn thuốc</a>
                </div>
                <div class="mb-3 me-3">
                    <a href="AppointmentServlet?action=GetBillsList" class="btn btn-success">Danh sách hóa đơn</a>
                </div>
            </div>
            <h2 class="text-center">List of Appointments</h2>
            <table class="table table-bordered align-middle text-center">
                <thead class="table-dark">
                    <tr>                      
                        <th>#</th>
                        <th>Patient Name</th>
                        <th>Appointment Date</th>
                        <th>Appointment Time</th>
                        <th>Doctor Name</th>
                        <th>Status</th>
                        <th>Notes</th>                                                
                        <th scope="col" style="width: 25%;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${appList}" var="e">
                        <tr>
                            <td>${e.appointmentID}</td>
                            <td>${e.patientID.fullName}</td>  
                            <td>${e.formatDate()}</td>
                            <td>${e.formatTime()}</td>
                            <td>${e.doctorID.fullName}</td>  
                            <td>${e.status}</td>  
                            <td>${e.notes}</td>  
                            <td>
                                <a href="MedicalServlet?appointmentId=${e.appointmentID}" class="btn btn-primary me-1">MedRecord</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table
        </div>
    </body>
</html>
