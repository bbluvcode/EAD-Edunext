<!DOCTYPE html>
<html>
<head>
    <title>Auth UI</title>
    <link rel="stylesheet" type="text/css" href="css/logincss.css">
    <link href="https://fonts.googleapis.com/css2?family=Jost:wght@500&display=swap" rel="stylesheet">
</head>
<body class="auth-body">
    <div class="auth-container">  	
        <input type="checkbox" id="toggle-forgot" aria-hidden="true">

        <div class="auth-login-panel">
            <form>
                <label for="toggle-forgot" aria-hidden="true">Login</label>
                <input type="email" name="login-email" placeholder="Email" required>
                <input type="password" name="login-password" placeholder="Password" required>
                <button class="auth-btn">Login</button>
            </form>
        </div>

        <div class="auth-forgot-panel">
            <form>
                <label for="toggle-forgot" aria-hidden="true">Forgot Password</label>
                <input type="email" name="forgot-email" placeholder="Email" required>
                <input type="password" name="new-password" placeholder="New Password" required>
                <button class="auth-btn">Submit</button>
            </form>
        </div>
    </div>
</body>
</html>
