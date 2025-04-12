/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.Doctors;
import entities.Patients;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author ACER
 */
@Stateless
public class LoginSB implements LoginSBLocal {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("EADEdunext");
    EntityManager em = emf.createEntityManager();

    @Override
    public Object login(String email, String password) {
        // Kiểm tra đăng nhập với Doctors
        TypedQuery<Doctors> doctorQuery = em.createNamedQuery("Doctors.findByEmail", Doctors.class);
        doctorQuery.setParameter("email", email);
        Doctors doctor = null;
        try {
            doctor = doctorQuery.getSingleResult();
            if (doctor != null && doctor.getPassword().equals(password)) {
                return doctor;
            }
        } catch (Exception e) {
            // Doctor không tồn tại, tiếp tục tìm trong Patients
        }

        // Kiểm tra đăng nhập với Patients
        TypedQuery<Patients> patientQuery = em.createNamedQuery("Patients.findByEmail", Patients.class);
        patientQuery.setParameter("email", email);
        Patients patient = null;
        try {
            patient = patientQuery.getSingleResult();
            if (patient != null && patient.getPassword().equals(password)) {
                return patient;
            }
        } catch (Exception e) {
            // Patient không tồn tại hoặc mật khẩu sai
        }

        // Nếu không tìm thấy người dùng hợp lệ
        return null;
    }

    @Override
    public boolean sendOtp(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
