<%-- Document : medical Created on : Apr 12, 2025, 12:43:28 PM Author : Admin --%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Medical Record</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
                    rel="stylesheet">
                <style>
                    /* Ensure the footer stays at the bottom */
                    html,
                    body {
                        height: 100%;
                        margin: 0;
                        display: flex;
                        flex-direction: column;
                    }

                    body {
                        background-color: #f8f9fa;
                        font-family: 'Jost', sans-serif;
                    }

                    .container {
                        flex: 1;
                        /* Pushes the footer to the bottom */
                    }

                    .navbar-transparent {
                        background: rgba(0, 0, 0, 0.5);
                    }

                    footer {

                        background: linear-gradient(to right, #0f2027, #203a43, #2c5364);
                        color: white;
                        padding: 10px 0;
                        text-align: center;
                    }
                </style>
            </head>

            <body>
                <!-- Navbar -->
                <nav class="navbar navbar-expand-lg navbar-dark navbar-transparent fixed-top">
                    <div class="container">
                        <a class="navbar-brand fw-bold" href="#">HealthSys</a>
                        <div class="ms-auto d-flex gap-2">
                            <!-- <a href="RegisterServlet" class="btn btn-success d-flex align-items-center gap-2">
                                Create <i class="bi bi-plus-lg"></i>
                            </a> -->
                            <% Object user=session.getAttribute("user"); if (user !=null) { %>
                                <a href="LogoutServlet" class="btn btn-outline-warning d-flex align-items-center gap-2">
                                    Logout <i class="bi bi-box-arrow-right"></i>
                                </a>
                                <% } else { %>
                                    <a href="LoginServlet"
                                        class="btn btn-outline-light d-flex align-items-center gap-2">
                                        Login <i class="bi bi-box-arrow-in-right"></i>
                                    </a>
                                    <% } %>
                        </div>
                    </div>
                </nav>

                <!-- Main Content -->
                <div class="container mt-5 pt-5">
                    <h1 class="text-center mb-4">Medical Record</h1>
                    <c:choose>
                        <c:when test="${not empty medicalRecord}">
                            <div class="row mb-4">
                                <!-- Medical Record Details -->
                                <div class="col-md-6">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Medical Record Details</h5>
                                            <p><strong>Symptoms:</strong> ${medicalRecord.symptoms}</p>
                                            <p><strong>Diagnosis:</strong> ${medicalRecord.diagnosis}</p>
                                            <p><strong>Created At:</strong> ${medicalRecord.createdAt}</p>
                                        </div>
                                    </div>
                                </div>
                                <!-- Patient Information -->
                                <div class="col-md-6">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Patient Information</h5>
                                            <p><strong>Full Name:</strong> ${patient.fullName}</p>
                                            <p><strong>Gender:</strong> ${patient.gender}</p>
                                            <p><strong>Year of Birth:</strong>
                                                <span id="dob">${patient.dateOfBirth}</span>
                                                (<span id="age"></span>)
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Form to update symptoms -->
                            <form action="MedicalServlet" method="post" class="mb-3">
                                <input type="hidden" name="action" value="updateSymptoms">
                                <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                <input type="hidden" name="appointmentId" value="${appointmentId}">
                                <div class="mb-3">
                                    <label for="newSymptoms" class="form-label">Update Symptoms</label>
                                    <input type="text" class="form-control" id="newSymptoms" name="newSymptoms"
                                        placeholder="Enter new symptoms" required>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-pencil"></i> Update
                                    Symptoms</button>
                            </form>
                            <!-- Form to update diagnosis -->
                            <form action="MedicalServlet" method="post" class="mb-3">
                                <input type="hidden" name="action" value="updateDiagnosis">
                                <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                <input type="hidden" name="appointmentId" value="${appointmentId}">
                                <div class="mb-3">
                                    <label for="newDiagnosis" class="form-label">Update Diagnosis</label>
                                    <input type="text" class="form-control" id="newDiagnosis" name="newDiagnosis"
                                        placeholder="Enter new diagnosis" required>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-pencil"></i> Update
                                    Diagnosis</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="MedicalServlet" method="post">
                                <input type="hidden" name="action" value="create">
                                <input type="hidden" name="appointmentId" value="${appointmentId}">
                                <div class="mb-3">
                                    <label for="symptoms" class="form-label">Symptoms</label>
                                    <input type="text" class="form-control" id="symptoms" name="symptoms"
                                        placeholder="Enter symptoms" required>
                                </div>
                                <div class="mb-3">
                                    <label for="diagnosis" class="form-label">Diagnosis</label>
                                    <input type="text" class="form-control" id="diagnosis" name="diagnosis"
                                        placeholder="Enter diagnosis" required>
                                </div>
                                <button type="submit" class="btn btn-success"><i class="bi bi-plus-circle"></i> Create
                                    Medical Record</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                    <!-- Back to appointments list -->
                    <div class="mt-4">
                        <a href="AppointmentServlet" class="btn btn-secondary"><i class="bi bi-arrow-left"></i> Back to
                            Appointments</a>
                    </div>
                </div>

                <!-- Footer -->
                <footer>
                    <p>&copy; 2025 Healthcare Management System. All rights reserved.</p>
                </footer>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script>
                    // JavaScript to extract the year and calculate age
                    document.addEventListener("DOMContentLoaded", function () {
                        const dobElement = document.getElementById("dob");
                        const ageElement = document.getElementById("age");

                        if (dobElement && dobElement.textContent) {
                            const dobString = dobElement.textContent.trim();

                            // Extract the year by slicing the last 4 characters
                            const year = parseInt(dobString.slice(-4), 10);

                            if (!isNaN(year)) { // Check if the year is valid
                                const currentYear = new Date().getFullYear(); // Get the current year
                                const age = currentYear - year;

                                // Update the content
                                dobElement.textContent = year; // Show only the year
                                ageElement.textContent = age + " yo"; // Show age in years
                            } else {
                                dobElement.textContent = "Invalid Date"; // Handle invalid date
                                ageElement.textContent = "";
                            }
                        }
                    });
                </script>
            </body>

            </html>