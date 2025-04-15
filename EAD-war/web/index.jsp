<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Healthcare Management System</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

        <style>
            html {
                scroll-behavior: smooth;
                height: 100%;
            }

            body, html {
                height: 100%;
                margin: 0;
                overflow: hidden; /* ?n thanh cu?n */
            }

            .hero-section {
                margin-top: 0;
                height: 60vh; /* Chi?m 60% chi?u cao màn hình */
                position: relative;
                overflow: hidden;
                background: linear-gradient(to bottom, #43cea2, #185a9d);
                border-radius: 0;
                box-shadow: 5px 20px 50px #000;
            }

            .overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.6);
            }

            .hero-content {
                position: relative;
                z-index: 1;
            }

            .hero-content h1,
            .hero-content p {
                color: #fff;
                text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.7);
            }

            .navbar-transparent {
                background: rgba(0, 0, 0, 0.5);
            }

            #features {
                height: 30vh; /* Chi?m ph?n còn l?i c?a màn hình */
                overflow: auto; /* Cho phép cu?n n?u c?n */
            }

            footer {
                height: 10vh;
                display: flex;
                align-items: center;
                justify-content: center;
                background: linear-gradient(to right, #0f2027, #203a43, #2c5364);
                color: white;
            }

        </style>


    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark navbar-transparent fixed-top">
            <div class="container">
                <a class="navbar-brand fw-bold" href="#">HealthSys</a>
                <div class="ms-auto d-flex gap-2">


                    <%
                        Object user = session.getAttribute("user");
                        if (user != null) {
                    %>
                    <a href="AppointmentServlet?action=GetPatient" class="btn btn-outline-success d-flex align-items-center gap-2">
                        <i class="bi bi-plus-lg"> Create Appointment</i>
                    </a>
                    <a href="AppointmentServlet?action=GetAppsByPatient" class="btn btn-outline-primary d-flex align-items-center gap-2">
                        Appointment History
                    </a>
                    <a href="LoginServlet?action=Logout" class="btn btn-outline-warning d-flex align-items-center gap-2">
                        Logout <i class="bi bi-box-arrow-right"></i>
                    </a>
                    <%
                    } else {
                    %>
                    <a href="LoginServlet" class="btn btn-outline-light d-flex align-items-center gap-2">
                        Login <i class="bi bi-box-arrow-in-right"></i>
                    </a>
                    <%
                        }
                    %>
                </div>
            </div>
        </nav>


        <!-- Hero Section -->
        <div class="hero-section d-flex align-items-center justify-content-center text-center">
            <div class="overlay"></div>
            <div class="hero-content container">
                <h1 class="display-4 fw-bold">Healthcare Management System</h1>
                <p class="lead">Connecting doctors, patients, medications, and appointments seamlessly</p>
                <a href="#features" class="btn btn-primary btn-lg mt-3">Learn More</a>
            </div>
        </div>

        <!-- Features Section -->
        <section id="features" class="py-5 bg-light">
            <div class="container">
                <div class="row g-4">
                    <div class="col-md-4">
                        <div class="card shadow h-100">
                            <div class="card-body text-center">
                                <i class="bi bi-person-badge fs-1 text-primary mb-3"></i>
                                <h4 class="card-title">Doctors</h4>
                                <p class="card-text">
                                    Empower healthcare professionals with tools to manage appointments, update patient records,
                                    and access medical histories in real time. Optimize workflow and ensure timely care delivery.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow h-100">
                            <div class="card-body text-center">
                                <i class="bi bi-people fs-1 text-success mb-3"></i>
                                <h4 class="card-title">Patients</h4>
                                <p class="card-text">
                                    Patients can easily book appointments, view prescriptions, and track their healthcare journey.
                                    Receive instant notifications, consult with doctors, and manage personal medical data effortlessly.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow h-100">
                            <div class="card-body text-center">
                                <i class="bi bi-capsule fs-1 text-danger mb-3"></i>
                                <h4 class="card-title">Medications</h4>
                                <p class="card-text">
                                    Centralized access to prescriptions, dosage instructions, and detailed drug information.
                                    Enhance medication adherence and safety with smart alerts and integrated pharmacy support.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>



        <!-- Footer -->
        <footer class="bg-dark text-white text-center py-3">
            &copy; 2025 Healthcare Management System. All rights reserved.
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
