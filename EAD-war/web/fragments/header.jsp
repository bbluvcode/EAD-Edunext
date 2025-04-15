<head>
    <title>Medical Record</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        html,
        body {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;
        }

        /* Header styling */
        .navbar {
            background-color: #203a43;
            /* Dark blue-gray background */
            border-bottom: 2px solid #2c5364;
            /* Optional border for separation */
        }

        .navbar .navbar-brand {
            color: #ffffff;
            /* White text for the brand */
            font-weight: bold;
        }

        .navbar .navbar-brand:hover {
            color: #d1e8e2;
            /* Light green on hover */
        }

        .navbar .btn {
            border-radius: 20px;
            /* Rounded buttons */
        }

        .navbar .btn-outline-light {
            color: #ffffff;
            /* White text for outline buttons */
            border-color: #ffffff;
        }

        .navbar .btn-outline-light:hover {
            background-color: #ffffff;
            color: #203a43;
            /* Dark text on hover */
        }

        .container {
            flex: 1;
        }

        /* Footer styling */
        footer {
            background: linear-gradient(to right, #0f2027, #203a43, #2c5364);
            color: white;
            padding: 10px 0;
            text-align: center;
        }
    </style>
</head>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="#">HealthSys</a>
        <div class="ms-auto d-flex gap-2">
            <% Object user=session.getAttribute("user"); if (user !=null) { %>
                <a href="LogoutServlet" class="btn btn-outline-light d-flex align-items-center gap-2">
                    Logout <i class="bi bi-box-arrow-right"></i>
                </a>
                <% } else { %>
                    <a href="LoginServlet" class="btn btn-outline-light d-flex align-items-center gap-2">
                        Login <i class="bi bi-box-arrow-in-right"></i>
                    </a>
                    <% } %>
        </div>
    </div>
</nav>