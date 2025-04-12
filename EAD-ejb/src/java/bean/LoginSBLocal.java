/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package bean;

import jakarta.ejb.Local;

/**
 *
 * @author ACER
 */
@Local
public interface LoginSBLocal {

    Object login(String email, String password);

    boolean sendOtp(String email);
}
