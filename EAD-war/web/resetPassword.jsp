<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Reset Password</title>
        <link rel="stylesheet" type="text/css" href="css/logincss.css">
        <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
    </head>
    <body class="auth-body">
        <div class="auth-container">
            <div class="auth-login-panel">
                <form action="LoginServlet" method="post">
                    <input type="hidden" name="action" value="ResetPassword">
                    <label aria-hidden="true">Reset Password</label>

                    <input type="password" name="newPassword" placeholder="New Password" required>
                    <input type="password" name="confirmPassword" placeholder="Confirm Password" required>

                    <button class="auth-btn" type="submit">Submit</button>

                    <% if (request.getAttribute("error") != null) {%>
                    <p style="color:red;text-align:center"><%= request.getAttribute("error")%></p>
                    <% } else if (request.getAttribute("message") != null) {%>
                    <p style="color:green;text-align:center"><%= request.getAttribute("message")%></p>
                    <% }%>
                </form>
            </div>
        </div>
    </body>
</html>
