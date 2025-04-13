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
<<<<<<< HEAD
                    <a href="AppointmentServlet?action=GetPatient" class="btn btn-success">Create Appointment</a>
                </div>
            </div>
            <h2 class="text-center">List of Appointments</h2>
          <table class="table table-bordered align-middle text-center">
=======
                    <a href="AppointmentServlet" class="btn btn-danger">Back</a>
                </div>
            </div>
            <h2 class="text-center">Danh sách hóa đơn</h2>
            <table class="table table-bordered align-middle text-center">
>>>>>>> dev
                <thead class="table-dark">
                    <tr>                      
                        <th>#</th>
                        <th>Patient Name</th>
                        <th>Amount</th>
                        <th>Payment Date</th>
                        <th>Payment Method</th>
                        <th>Doctor Name</th>
                        <th>Status</th>                                               
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${bList}" var="e">
                        <tr>
                            <td>${e.billID}</td>
                            <td>${e.appointmentID.patientID.fullName}</td> 
                            <td>${e.amount}</td>  
                            <td>${e.formatDate()}</td>
                            <td>${e.paymentMethod}</td>
                            <td>${e.appointmentID.doctorID.fullName}</td>  
                            <td>${e.status}</td>  
                            <td>
                                <a href="AppointmentServlet?action=Cancel&id=${e.appointmentID}" class="btn btn-danger" 
                                   onClick="return confirm('Are you sure want to Cancel Appointment?')">Cancel</a>
                            </td>  
                        </tr>
                    </c:forEach>
                </tbody>
            </table
        </div>
    </body>
</html>
