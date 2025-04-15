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
                        <h1 class="">Medical Record</h1>
                        <div class="right-title">
                            <a href="" class="btn btn-success">
                                Finish <i class="bi bi-bookmark-check"></i>
                            </a>
                            <a href="AppointmentServlet" class="btn btn-secondary">
                                Back <i class="bi bi-arrow-right"></i>
                            </a>
                        </div>
                    </div>
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
                            <div class="button-group">
                                <!-- Button to toggle Prescription List -->
                                <button id="togglePrescriptionListBtn" class="btn btn-primary mt-3">
                                    <i class="bi bi-view-list"></i> Prescription List
                                </button>

                                <!-- Button to toggle Update Symptoms Form -->
                                <button id="toggleUpdateSymptomsFormBtn" class="btn btn-secondary mt-3">
                                    <i class="bi bi-pencil"></i> Update Symptoms Form
                                </button>

                                <!-- Button to toggle Update Diagnosis Form -->
                                <button id="toggleUpdateDiagnosisFormBtn" class="btn btn-secondary mt-3">
                                    <i class="bi bi-pencil"></i> Update Diagnosis Form
                                </button>
                            </div>

                            <!-- Prescription List -->
                            <div class="row my-4 prescription-list" id="prescriptionList"
                                style="${not empty prescriptions ? '' : 'display: none;'}">
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
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="prescription" items="${prescriptions}"
                                                        varStatus="status">
                                                        <tr>
                                                            <td>${status.index + 1}</td>
                                                            <td>${prescription.medicineID.medicineName}</td>
                                                            <td>${prescription.dosage}</td>
                                                            <td>${prescription.quantity}</td>
                                                            <td>${prescription.duration}</td>
                                                            <td>
                                                                <button class="btn btn-warning btn-sm update-btn"
                                                                    data-id="${prescription.prescriptionID}">
                                                                    <i class="bi bi-pencil"></i>
                                                                </button>
                                                                <button class="btn btn-danger btn-sm delete-btn"
                                                                    data-id="${prescription.prescriptionID}">
                                                                    <i class="bi bi-trash"></i>
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <button id="showPrescriptionFormBtn" class="btn btn-primary mt-3">
                                                <i class="bi bi-plus-circle"></i> Add Prescription
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Update Symptoms Form -->
                            <div class="row my-4 card shadow" id="updateSymptomsForm" style="display: none;">
                                <form action="MedicalServlet" method="post" class="mb-3">
                                    <input type="hidden" name="action" value="updateSymptoms">
                                    <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                    <input type="hidden" name="appointmentId" value="${appointmentId}">
                                    <div class="my-3 input-group">
                                        <input type="text" class="form-control" id="newSymptoms" name="newSymptoms"
                                            placeholder="Enter new symptoms" required>
                                            <button type="submit" class="btn btn-primary"><i class="bi bi-pencil"></i> Update
                                                Symptoms</button>
                                            </div>
                                </form>
                            </div>

                            <!-- Update Diagnosis Form -->
                            <div class="row my-4 card shadow" id="updateDiagnosisForm" style="display: none;">
                                <form action="MedicalServlet" method="post" class="mb-3">
                                    <input type="hidden" name="action" value="updateDiagnosis">
                                    <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                    <input type="hidden" name="appointmentId" value="${appointmentId}">
                                    <div class="my-3 input-group">
                                        <input type="text" class="form-control" id="newDiagnosis" name="newDiagnosis"
                                            placeholder="Enter new diagnosis" required>
                                            <button type="submit" class="btn btn-primary"><i class="bi bi-pencil"></i> Update
                                                Diagnosis</button>
                                    </div>
                                </form>
                            </div>
                            
                            <div class="row mb-4 prescription-form" id="prescriptionForm" style="display: none;">
                                <div class="col-md-12">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Add Prescription</h5>
                                            <form action="PrescriptionServlet" method="post">
                                                <input type="hidden" name="recordId" value="${medicalRecord.recordID}">
                                                <input type="hidden" name="appointmentId" value="${appointmentId}">

                                                <div class="mb-3">
                                                    <label for="medicineId" class="form-label">Medicine</label>
                                                    <select class="form-control" id="medicineId" name="medicineId"
                                                        required>
                                                        <!-- Populate with medicines from the database -->
                                                        <c:forEach var="medicine" items="${medicines}">
                                                            <option value="${medicine.medicineID}">
                                                                ${medicine.medicineName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="dosage" class="form-label">Dosage</label>
                                                    <input type="text" class="form-control" id="dosage" name="dosage"
                                                        required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="quantity" class="form-label">Quantity</label>
                                                    <input type="number" class="form-control" id="quantity"
                                                        name="quantity" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="duration" class="form-label">Duration (days)</label>
                                                    <input type="number" class="form-control" id="duration"
                                                        name="duration" required>
                                                </div>
                                                <button type="submit" class="btn btn-success">Add
                                                    Prescription</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-4 update-prescription-form" id="updatePrescriptionForm"
                                style="display: none;">
                                <div class="col-md-12">
                                    <div class="card shadow">
                                        <div class="card-body">
                                            <h5 class="card-title text-primary">Update Prescription</h5>
                                            <form action="PrescriptionServlet" method="post">
                                                <input type="hidden" name="appointmentId" value="${appointmentId}">

                                                <input type="hidden" name="action" value="update">
                                                <input type="hidden" id="updatePrescriptionId" name="prescriptionId">
                                                <div class="mb-3">
                                                    <label for="updateMedicineId" class="form-label">Medicine</label>
                                                    <select class="form-control" id="updateMedicineId" name="medicineId"
                                                        required>
                                                        <c:forEach var="medicine" items="${medicines}">
                                                            <option value="${medicine.medicineID}">
                                                                ${medicine.medicineName}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="updateDosage" class="form-label">Dosage</label>
                                                    <input type="text" class="form-control" id="updateDosage"
                                                        name="dosage" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="updateQuantity" class="form-label">Quantity</label>
                                                    <input type="number" class="form-control" id="updateQuantity"
                                                        name="quantity" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="updateDuration" class="form-label">Duration
                                                        (days)</label>
                                                    <input type="number" class="form-control" id="updateDuration"
                                                        name="duration" required>
                                                </div>
                                                <button type="submit" class="btn btn-success">Update
                                                    Prescription</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                          
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

                        } else {
                            form.style.display = "none";
                        }
                    });

                    // Handle Update Button Click
                    document.querySelectorAll('.update-btn').forEach(button => {
                        button.addEventListener('click', function () {
                            console.log("🚀 ~ this.dataset:", this.dataset);
                            const form = document.getElementById('updatePrescriptionForm');
                            if (form.style.display === "none") {
                                // Populate the form fields with the data from the button
                                document.getElementById('updatePrescriptionId').value = this.dataset.id;
                                document.getElementById('updateDosage').value = this.dataset.dosage;
                                document.getElementById('updateQuantity').value = this.dataset.quantity;
                                document.getElementById('updateDuration').value = this.dataset.duration;

                                // Set the correct medicine in the dropdown
                                const medicineSelect = document.getElementById('updateMedicineId');
                                const medicineValue = this.dataset.medicine; // Get the medicine name or ID from the button
                                Array.from(medicineSelect.options).forEach(option => {
                                    if (option.value === medicineValue) {
                                        option.selected = true;
                                    }
                                });

                                // Show the form
                                form.style.display = "block";
                            } else {
                                form.style.display = "none";
                            }

                        });
                    });

                    // Handle Delete Button Click
                    document.querySelectorAll('.delete-btn').forEach(button => {
                        button.addEventListener('click', function () {
                            if (confirm('Are you sure you want to delete this prescription?')) {
                                const prescriptionId = this.dataset.id;
                                console.log("🚀 ~ prescriptionId:", prescriptionId)
                                fetch("PrescriptionServlet?action=delete&prescriptionId=" + prescriptionId, {
                                    method: 'GET'
                                })
                                    .then(response => {
                                        if (response.ok) {
                                            location.reload(); // Reload the page after deletion
                                        } else {
                                            alert('Failed to delete prescription.');
                                        }
                                    })
                                    .catch(error => console.error('Error:', error));
                            }
                        });
                    });

                    // Toggle Prescription List
                    document.getElementById("togglePrescriptionListBtn").addEventListener("click", function () {
                        const prescriptionList = document.getElementById("prescriptionList");
                        if (prescriptionList.style.display === "none") {
                            prescriptionList.style.display = "block";
                            this.innerHTML = '<i class="bi bi-eye-slash"></i> Hide Prescription List';
                        } else {
                            prescriptionList.style.display = "none";
                            this.innerHTML = '<i class="bi bi-view-list"></i> Show Prescription List';
                        }
                    });

                    // Toggle Update Symptoms Form
                    document.getElementById("toggleUpdateSymptomsFormBtn").addEventListener("click", function () {
                        const updateSymptomsForm = document.getElementById("updateSymptomsForm");
                        if (updateSymptomsForm.style.display === "none") {
                            updateSymptomsForm.style.display = "block";
                            this.innerHTML = '<i class="bi bi-eye-slash"></i> Hide Update Symptoms Form';
                        } else {
                            updateSymptomsForm.style.display = "none";
                            this.innerHTML = '<i class="bi bi-pencil"></i> Show Update Symptoms Form';
                        }
                    });

                    // Toggle Update Diagnosis Form
                    document.getElementById("toggleUpdateDiagnosisFormBtn").addEventListener("click", function () {
                        const updateDiagnosisForm = document.getElementById("updateDiagnosisForm");
                        if (updateDiagnosisForm.style.display === "none") {
                            updateDiagnosisForm.style.display = "block";
                            this.innerHTML = '<i class="bi bi-eye-slash"></i> Hide Update Diagnosis Form';
                        } else {
                            updateDiagnosisForm.style.display = "none";
                            this.innerHTML = '<i class="bi bi-pencil"></i> Show Update Diagnosis Form';
                        }
                    });
                </script>
            </body>

            </html>