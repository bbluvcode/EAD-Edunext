/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.AppointmentSBLocal;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/get-booked-time")
public class GetBookedTimesServlet extends HttpServlet {

    @EJB
    private AppointmentSBLocal sb;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int doctorId = Integer.parseInt(request.getParameter("doctorId"));
        String date = request.getParameter("date");

        List<String> bookedTimes = sb.getBookedTimeSlots(doctorId, date);

        response.setContentType("application/json");
        new Gson().toJson(bookedTimes, response.getWriter());
    }
}
