/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import bean.LoginSBLocal;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ACER
 */
public class LoginServlet extends HttpServlet {

    @EJB
    LoginSBLocal sb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String action = request.getParameter("action");
            if (null == action) {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                switch (action) {
                    case "Login":
                        String email = request.getParameter("email");
                        String pass = request.getParameter("password");
                        Object user = sb.login(email, pass);
                        if (user != null) {
                            request.getSession().setAttribute("user", user);
                            if (user instanceof entities.Doctors) {
                                response.sendRedirect("doctorHome.jsp");
                            } else if (user instanceof entities.Patients) {
                                response.sendRedirect("patientHome.jsp");
                            }
                        } else {
                            request.setAttribute("error", "Invalid email or password.");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                        break;

                    case "SendOTP":
                        String emailToSend = request.getParameter("email");
                        String generatedOtp = String.valueOf((int) (Math.random() * 900000) + 100000); // 6 chữ số

                        if (sb.sendOtp(emailToSend, generatedOtp)) {
                            request.getSession().setAttribute("otpEmail", emailToSend); // lưu lại để so OTP và reset pass
                            request.getSession().setAttribute("otpCode", generatedOtp); // lưu mã OTP
                            request.setAttribute("message", "OTP has been sent to your email.");
                        } else {
                            request.setAttribute("error", "Email not found.");
                        }

                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        break;

                    case "VerifyOTP":
                        String enteredOtp = request.getParameter("otp");
                        String sessionOtp = (String) request.getSession().getAttribute("otpCode");

                        if (enteredOtp != null && enteredOtp.equals(sessionOtp)) {
                            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Invalid OTP.");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                        break;

                    case "ResetPassword":
                        String newPass = request.getParameter("newPassword");
                        String confirmPass = request.getParameter("confirmPassword");
                        String emailToReset = (String) request.getSession().getAttribute("otpEmail");

                        if (emailToReset == null) {
                            request.setAttribute("error", "Session expired. Please try again.");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                            return;
                        }

                        if (!newPass.equals(confirmPass)) {
                            request.setAttribute("error", "Passwords do not match.");
                            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                            return;
                        }

                        if (sb.updatePassword(emailToReset, newPass)) {
                            request.setAttribute("message", "Password updated. Please login.");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Failed to update password.");
                            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
