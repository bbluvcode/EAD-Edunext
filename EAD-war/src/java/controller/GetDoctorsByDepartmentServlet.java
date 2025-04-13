/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.*;
import entities.*;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet("/get-doctors")
public class GetDoctorsByDepartmentServlet extends HttpServlet {

    @EJB
    private AppointmentSBLocal sb;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String department = request.getParameter("department");
        List<Doctors> doctors = sb.getDoctorsBySpecialization(department);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String selectedId = request.getParameter("selected");
        for (Doctors d : doctors) {
            if (String.valueOf(d.getDoctorID()).equals(selectedId)) {
                out.println("<option value=\"" + d.getDoctorID() + "\" selected>" + d.getFullName() + "</option>");
            } else {
                out.println("<option value=\"" + d.getDoctorID() + "\">" + d.getFullName() + "</option>");
            }
        }
    }
}
