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
        <div class="container mt-4 w-75">
            <div class="d-flex align-items-center gap-2">
                <div class="mb-3 me-4">
                    <a href="AppointmentServlet" class="btn btn-danger">Back</a>
                </div>
                <div class="mb-3 me-4">
                    <a href="AppointmentServlet?action=CreatePrescription" class="btn btn-success">Tạo đơn thuốc</a>
                </div>
            </div>
            <h2 class="text-center mb-3">Danh sách đơn thuốc</h2>
            <table class="table table-bordered align-middle text-center">
                <thead class="table-dark">
                    <tr>                      
                        <th>#</th>
                        <th>Patient Name</th>
                        <th>Record ID</th>
                        <th>Medicine Name</th> 
                        <th>Quantity</th>
                        <th>Dosage</th>
                        <th>Duration</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${pList}" var="e">
                        <tr>
                            <td>${e.prescriptionID}</td>
                            <td>${e.recordID.appointmentID.patientID.fullName}</td> 
                            <td>${e.recordID.recordID}</td>  
                            <td>${e.medicineID.medicineName}</td>
                            <td>${e.quantity}</td>
                            <td>${e.dosage}</td>  
                            <td>${e.duration}</td>   
                        </tr>
                    </c:forEach>
                </tbody>
            </table
        </div>
    </body>
</html>
