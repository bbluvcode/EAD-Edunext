<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Medical History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="fragments/header.jsp" />

    <!-- Main Content -->
    <div class="container mt-5">
        <h1 class="text-center mb-4">Medical History</h1>

        <!-- Search Form -->
        <form action="MedicalHistoryServlet" method="get" class="mb-4">
            <input type="hidden" name="patientId" value="${param.patientId}">
            <input type="hidden" name="appointmentId" value="${param.appointmentId}">
            <div class="input-group">
                <input type="text" name="searchQuery" class="form-control" placeholder="Search by symptoms, diagnosis, or record ID" value="${param.searchQuery}">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-search"></i> Search
                </button>
            </div>
        </form>

        <!-- Medical History Table -->
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Record ID</th>
                    <th>Symptoms</th>
                    <th>Diagnosis</th>
                    <th>Created At</th>
                    <th>Prescription</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="record" items="${medicalHistory}">
                    <tr>
                        <td>${record.recordID}</td>
                        <td>${record.symptoms}</td>
                        <td>${record.diagnosis}</td>
                        <td>${record.createdAt}</td>
                        <td>
                            <a class="btn btn-info">View</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="MedicalServlet?appointmentId=${appointmentId}" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Back to Medical Record
        </a>
    </div>

    <!-- Include Footer -->
    <jsp:include page="fragments/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>