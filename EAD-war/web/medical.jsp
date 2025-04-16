<%-- Document : medical Created on : Apr 12, 2025, 12:43:28 PM Author : Admin --%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Medical Record</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
                    rel="stylesheet">
                <style>
                    .patient-info-card {
                        position: relative;
                    }

                    .btnviewmedicalhistory {
                        position: absolute;
                        right: 1rem;
                        top: 0;
                    }
                </style>
            </head>

            <body>
                <!-- Include Header -->
                <jsp:include page="fragments/header.jsp" />

                <!-- Main Content -->
                <div class="container pt-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h1 class="">Medical Recorddd</h1>
                        <div class="right-title">
                            <a href="" class="btn btn-success">
                                Finish <i class="bi bi-bookmark-check"></i>
                            </a>
                            <a href="AppointmentServlet" class="btn btn-secondary">
                                Back <i class="bi bi-arrow-right"></i>
                            </a>
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