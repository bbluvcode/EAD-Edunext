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
            <div class="d-flex justify-content-start">
                <a href="index.jsp" class="btn btn-danger">← Back</a>
            </div>
            <h2 class="text-center mb-3">Appointments History</h2>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <table class="table table-bordered table-stripped align-middle text-center">
                <thead class="table-dark">
                    <tr>                      
                        <th>#</th>
                        <th>Appointment Date</th>
                        <th>Appointment Time</th>
                        <th>Doctor Name</th>
                        <th>Status</th>
                        <th>Notes</th>                                                
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${aList}" var="e" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td> 
                            <td>${e.formatDate()}</td>
                            <td>${e.formatTime()}</td>
                            <td>${e.doctorID.fullName}</td>  
                            <td>${e.status}</td>  
                            <td>${e.notes}</td>  
                            <td>
                                <c:choose>
                                    <c:when test="${not empty e.billsList}">
                                        <a href="AppointmentServlet?action=GetBill&appointmentId=${e.appointmentID}" class="btn btn-primary me-2">Detail</a>
                                        <button class="btn btn-danger me-2" disabled>Cancel</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-primary me-2" disabled>Detail</button>
                                        <a href="AppointmentServlet?action=Cancel&appointmentId=${e.appointmentID}" class="btn btn-danger me-2"
                                           onclick="return confirm('Bạn có chắc chắn muốn hủy lịch hẹn này không?')"> Cancel </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
