<%-- 
    Document   : medical
    Created on : Apr 12, 2025, 12:43:28 PM
    Author     : Admin
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Medical Record</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4 w-50">
            <h1 class="text-center">Medical Record</h1>
            <!-- Start of conditional rendering -->
            <c:choose>
                <c:when test="${not empty medicalRecord}">
                    <div class="card mb-4">
                        <div class="card-body">
                            <h5 class="card-title">Medical Record Details</h5>
                            <p><strong>Symptoms:</strong> ${medicalRecord.symptoms}</p>
                            <p><strong>Diagnosis:</strong> ${medicalRecord.diagnosis}</p>
                            <p><strong>Created At:</strong> ${medicalRecord.createdAt}</p>
                        </div>
                    </div>
                    <!-- Form to update symptoms -->
                    <form action="MedicalServlet" method="post" class="mb-3">
                        <input type="hidden" name="action" value="updateSymptoms">
                        <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentID}">
                        <div class="mb-3">
                            <label for="newSymptoms" class="form-label">Update Symptoms</label>
                            <input type="text" class="form-control" id="newSymptoms" name="newSymptoms" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Symptoms</button>
                    </form>
                    <!-- Form to update diagnosis -->
                    <form action="MedicalServlet" method="post" class="mb-3">
                        <input type="hidden" name="action" value="updateDiagnosis">
                        <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentID}">
                        <div class="mb-3">
                            <label for="newDiagnosis" class="form-label">Update Diagnosis</label>
                            <input type="text" class="form-control" id="newDiagnosis" name="newDiagnosis" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Diagnosis</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="MedicalServlet" method="post">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="appointmentId" value="${appointmentId}">
                        <div class="mb-3">
                            <label for="symptoms" class="form-label">Symptoms</label>
                            <input type="text" class="form-control" id="symptoms" name="symptoms" required>
                        </div>
                        <div class="mb-3">
                            <label for="diagnosis" class="form-label">Diagnosis</label>
                            <input type="text" class="form-control" id="diagnosis" name="diagnosis" required>
                        </div>
                        <button type="submit" class="btn btn-success">Create Medical Record</button>
                    </form>
                </c:otherwise>
            </c:choose>
            <!-- Back to appointments list -->
            <div class="mt-4">
                <a href="DoctorServlet" class="btn btn-secondary">Back to Appointments</a>
            </div>
        </div>
    </body>
</html>