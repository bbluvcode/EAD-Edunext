<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entities.Doctors" %>
<%@ page import="java.util.*" %>
<%@ page import="entities.Appointments" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    Doctors doctor = (Doctors) request.getAttribute("doctor");
    if (doctor == null) {
        doctor = (Doctors) session.getAttribute("doctor");
    }
    List<Appointments> appointments = (List<Appointments>) request.getAttribute("appointments");
    if (appointments != null) {
        // Sắp xếp từ ngày gần nhất đến xa nhất
        appointments.sort(( a2,           a1) -> a2.getAppointmentDate().compareTo(a1.getAppointmentDate()));
    }
    // Dữ liệu cho ChartJS
    Map<String, Integer> appointmentByDate = new LinkedHashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    if (appointments != null) {
        for (Appointments a : appointments) {
            String dateStr = sdf.format(a.getAppointmentDate());
            appointmentByDate.put(dateStr, appointmentByDate.getOrDefault(dateStr, 0) + 1);
        }
    }
%>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.ZoneId" %>
<%
    // Lấy ngày hôm nay
    LocalDate today = LocalDate.now();
    List<String> todayAppointments = new ArrayList<>();
    if (appointments != null) {
        for (Appointments a : appointments) {
            LocalDate appDate = a.getAppointmentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (appDate.equals(today)) {
                todayAppointments.add(a.formatTime());
            }
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Doctor Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            .circle-card {
                width: 180px;
                height: 180px;
                border-radius: 50%;
                background-color: #0d6efd;
                color: white;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 24px;
                font-weight: bold;
                box-shadow: 0 4px 10px rgba(0,0,0,0.2);
                margin: auto;
            }
        </style>
    </head>
    <body>
        <div class="container mt-5">
            <div class="mt-4">
                <button class="btn btn-secondary" onclick="history.back()">← Back</button>
            </div>
            <h2 class="text-center mb-4"><%= doctor.getFullName()%> Detail</h2>
            <div class="row">
                <!-- Personal Information (on the left) -->
                <div class="col-md-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            Personal Info
                        </div>
                        <div class="card-body">
                            <p><strong>Full Name:</strong> <%= doctor.getFullName()%></p>
                            <p><strong>Email:</strong> <%= doctor.getEmail()%></p>
                            <p><strong>Specialization:</strong> <%= doctor.getSpecialization()%></p>
                            <p><strong>Phone:</strong> <%= doctor.getPhone()%></p>
                        </div>
                    </div>
                </div>

                <!-- Today's Appointments (on the right) -->
                <div class="col-md-8 d-flex align-items-center justify-content-center">
                    <div class="circle-card bg-success">
                        <div class="text-center">
                            <div style="font-size: 18px;">Today's Appointments:</div>
                            <% if (!todayAppointments.isEmpty()) { %>
                            <% for (String time : todayAppointments) {%>
                            <div><%= time%></div>
                            <% } %>
                            <% } else { %>
                            <div>0</div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Chart and Table -->
            <div class="row mt-4">
                <!-- ChartJS Column Chart -->
                <div class="col-md-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-success text-white">
                            Appointments per Day
                        </div>
                        <div class="card-body">
                            <canvas id="appointmentsChart"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Appointment Table -->
                <div class="col-md-8">
                    <div class="card shadow-sm">
                        <div class="card-header bg-info text-white">
                            Appointment Details
                        </div>
                        <div class="card-body table-responsive">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Time</th>
                                        <th>Patient</th>
                                        <th>Note</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (appointments != null) {
                                            for (Appointments a : appointments) {%>
                                    <tr>
                                        <td><%= a.formatDate()%></td>
                                        <td><%= a.formatTime()%></td>
                                        <td><%= a.getPatientID() != null ? a.getPatientID().getFullName() : "N/A"%></td>
                                        <td><%= a.getNotes() != null ? a.getNotes() : "N/A"%></td>
                                        <td>
                                            <form action="AppointmentServlet" method="get" class="d-inline">
                                                <button class="btn btn-sm btn-outline-primary" name="action" value="view">Appointment</button>
                                            </form>
                                            <form action="MedicineServlet" method="get" class="d-inline">
                                                <button class="btn btn-sm btn-outline-success" name="action" value="view">Medicine</button>
                                            </form>
                                            <form action="PrescriptionServlet" method="get" class="d-inline">
                                                <button class="btn btn-sm btn-outline-warning" name="action" value="view">Prescription</button>
                                            </form>
                                            <form action="BillServlet" method="get" class="d-inline">
                                                <button class="btn btn-sm btn-outline-danger" name="action" value="view">Bill</button>
                                            </form>
                                        </td>
                                    </tr>
                                    <%  }
                                    } else { %>
                                    <tr><td colspan="3" class="text-center">No appointments found.</td></tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- ChartJS Script -->
        <script>
            const ctx = document.getElementById('appointmentsChart').getContext('2d');
            const appointmentsChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [<% for (String date : appointmentByDate.keySet()) {%>'<%= date%>', <% } %>],
                            datasets: [{
                                    label: 'Number of Appointments',
                                    data: [<% for (int count : appointmentByDate.values()) {%><%= count%>, <% }%>],
                                    backgroundColor: 'rgba(13, 110, 253, 0.6)',
                                    borderColor: 'rgba(13, 110, 253, 1)',
                                    borderWidth: 1,
                                    borderRadius: 4
                                }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            stepSize: 1
                        }
                    }
                }
            });
        </script>
    </body>
</html>
