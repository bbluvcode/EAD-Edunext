<!DOCTYPE html>
<html>
    <head>
        <title>Auth</title>
        <link rel="stylesheet" type="text/css" href="css/logincss.css">
        <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
    </head>
    <body class="auth-body">
        <div class="auth-container">
            <input type="checkbox" id="toggle-forgot" aria-hidden="true" 
                   <%= request.getAttribute("showForgotPassword") != null ? "checked" : ""%> />

            <div class="auth-login-panel">
                <form action="LoginServlet" method="post">
                    <input type="hidden" name="action" value="Login">
                    <label for="toggle-forgot" aria-hidden="true">Login</label>
                    <input type="email" name="email" placeholder="Email" required
                           value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>">
                    <input type="password" name="password" placeholder="Password" required>
                    <button class="auth-btn" type="submit">Login</button>

                    <% if (request.getAttribute("error") != null) {%>
                    <p style="color:red;text-align:center"><%= request.getAttribute("error")%></p>
                    <% } else if (request.getAttribute("message") != null) {%>
                    <p style="color:green;text-align:center"><%= request.getAttribute("message")%></p>
                    <% }%>
                </form>
            </div>

            <div class="auth-forgot-panel">
                <form action="LoginServlet" method="post">
                    <label for="toggle-forgot" aria-hidden="true">Forgot Password</label>

                    <div class="auth-input-group">
                        <input type="email" name="email" placeholder="Enter your email"
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>">
                        <button type="submit" name="action" value="SendOTP">Send OTP</button>
                    </div>

                    <input type="text" name="otp" class="auth-input-wide" placeholder="Enter OTP">
                    <button type="submit" name="action" value="VerifyOTP" class="auth-btn">Submit OTP</button>

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
