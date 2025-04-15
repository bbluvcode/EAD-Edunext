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
                                        <div class="card-body patient-info-card">
                                            <h5 class="card-title text-primary">Patient Information</h5>
                                            <p><strong>Full Name:</strong> ${patient.fullName}</p>
                                            <p><strong>Gender:</strong> ${patient.gender}</p>
                                            <p><strong>Year of Birth:</strong>
                                                <span id="dob">${patient.dateOfBirth}</span>
                                                (<span id="age"></span>)
                                            </p>
                                            <!-- Button to view medical record history -->
                                            <a href="MedicalHistoryServlet?patientId=${patient.patientID}&appointmentId=${appointmentId}"
                                                class="btn btn-info mt-3 btnviewmedicalhistory">
                                                <i class="bi bi-clock-history"></i> View Medical History
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-4 prescription-list">
                                <div class="col-md-12">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Prescriptions</h5>
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>No.</th>
                                                        <th>Medicine</th>
                                                        <th>Dosage</th>
                                                        <th>Quantity</th>
                                                        <th>Duration</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="prescription" items="${prescriptions}" varStatus="status">
                                                        <tr>
                                                            <td>${status.index + 1}</td>
                                                            <td>${prescription.medicineID.medicineName}</td>
                                                            <td>${prescription.dosage}</td>
                                                            <td>${prescription.quantity}</td>
                                                            <td>${prescription.duration}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <!-- Add Prescription Button -->
                                            <button id="showPrescriptionFormBtn" class="btn btn-primary mt-3">
                                                <i class="bi bi-plus-circle"></i> Add Prescription
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-4 prescription-form" id="prescriptionForm" style="display: none;">
                                <div class="col-md-12">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Add Prescription</h5>
                                            <form action="PrescriptionServlet" method="post">
                                                <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                                <div class="mb-3">
                                                    <label for="medicineId" class="form-label">Medicine</label>
                                                    <select class="form-control" id="medicineId" name="medicineId" required>
                                                        <!-- Populate with medicines from the database -->
                                                        <c:forEach var="medicine" items="${medicines}">
                                                            <option value="${medicine.medicineID}">${medicine.medicineName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="dosage" class="form-label">Dosage</label>
                                                    <input type="text" class="form-control" id="dosage" name="dosage" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="quantity" class="form-label">Quantity</label>
                                                    <input type="number" class="form-control" id="quantity" name="quantity" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="duration" class="form-label">Duration (days)</label>
                                                    <input type="number" class="form-control" id="duration" name="duration" required>
                                                </div>
                                                <button type="submit" class="btn btn-success">Add Prescription</button>
                                            </form>
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

                <!-- Include Footer -->
                <jsp:include page="fragments/footer.jsp" />

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

                    // JavaScript to toggle the visibility of the prescription form
                    document.getElementById("showPrescriptionFormBtn").addEventListener("click", function () {
                        const form = document.getElementById("prescriptionForm");
                        const medicineSelect = document.getElementById("medicineId");

                        // Toggle the visibility of the form
                        if (form.style.display === "none") {
                            form.style.display = "block";

                            // Make an AJAX call to fetch medicines
                            fetch("PrescriptionServlet?action=getMedicines")
                                .then(response => response.json())
                                .then(data => {
                                    // Clear existing options
                                    medicineSelect.innerHTML = "";

                                    // Populate the select element with new options
                                    data.forEach(medicine => {
                                        const option = document.createElement("option");
                                        option.value = medicine.medicineID;
                                        option.textContent = medicine.medicineName;
                                        medicineSelect.appendChild(option);
                                    });
                                })
                                .catch(error => console.error("Error fetching medicines:", error));
                        } else {
                            form.style.display = "none";
                        }
                    });
                </script>
            </body>

            </html>