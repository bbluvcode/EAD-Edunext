<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Doctors Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">

        <div class="container mt-5">
            <h2 class="mb-4 text-center">Doctors Management</h2>
            <div class="d-flex justify-content-between  mb-3">
                <a href="LoginServlet?action=Logout" class="btn btn-danger">Logout</a>
                <a href="DoctorServlet?action=create" class="btn btn-success">+ Create Doctor</a>
            </div>

            <table class="table table-hover table-bordered shadow-sm bg-white">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Specialization</th>
                        <th>Phone</th>
                        <th>Appointments</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${doctors}" var="d">
                        <tr>
                            <td>${d.doctorID}</td>
                            <td>${d.fullName}</td>
                            <td>${d.email}</td>
                            <td>${d.specialization}</td>
                            <td>${d.phone}</td>
                            <td>${appointmentCounts[d.doctorID]}</td>
                            <td>
                                <a href="DoctorServlet?action=detail&id=${d.doctorID}" class="btn btn-sm btn-info me-1">Detail</a>
                                <a href="DoctorServlet?action=update&id=${d.doctorID}" class="btn btn-sm btn-warning">Update</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
