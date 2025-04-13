<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entities.Doctors"%>
<%
    Doctors doc = (Doctors) request.getAttribute("doctor");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Update Doctor</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <div class="mx-auto" style="max-width: 600px;">
                <h2 class="mb-4 text-center">Update Doctor</h2>
                <form action="DoctorServlet?action=updateSubmit" method="post">
                    <input type="hidden" name="doctorID" value="<%=doc.getDoctorID()%>">
                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input type="text" class="form-control" name="fullName" value="<%=doc.getFullName()%>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" value="<%=doc.getEmail()%>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Specialization</label>
                        <input type="text" class="form-control" name="specialization" value="<%=doc.getSpecialization()%>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input type="text" class="form-control" name="phone" value="<%=doc.getPhone()%>" required>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-primary">Update</button>
                        <a href="DoctorServlet" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>