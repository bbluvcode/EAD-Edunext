/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.Doctors;
import entities.Patients;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.*;

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
    public boolean sendOtp(String email, String otp) {
        // Kiểm tra email tồn tại trong DB
        boolean exists = false;
        try {
            TypedQuery<Doctors> dq = em.createNamedQuery("Doctors.findByEmail", Doctors.class);
            dq.setParameter("email", email);
            exists = dq.getSingleResult() != null;
        } catch (Exception e) {
        }

        if (!exists) {
            try {
                TypedQuery<Patients> pq = em.createNamedQuery("Patients.findByEmail", Patients.class);
                pq.setParameter("email", email);
                exists = pq.getSingleResult() != null;
            } catch (Exception e) {
            }
        }

        if (!exists) {
            return false;
        }

        // Gửi email
        final String from = "buitamman1231@gmail.com";
        final String password = "ovoj vpno ptfa vjcd";
        String subject = "Your OTP Code";
        String body = "Your OTP code is: " + otp;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        try {
            em.getTransaction().begin(); // Bắt đầu transaction

            // Thử tìm Doctor
            TypedQuery<Doctors> dq = em.createNamedQuery("Doctors.findByEmail", Doctors.class);
            dq.setParameter("email", email);
            Doctors doctor = dq.getSingleResult();
            if (doctor != null) {
                doctor.setPassword(newPassword);
                em.merge(doctor);
                em.getTransaction().commit(); // Commit thay đổi
                return true;
            }

        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback nếu lỗi
            // Không tìm thấy Doctor, thử Patient
        }

        try {
            em.getTransaction().begin(); // Bắt đầu transaction mới

            TypedQuery<Patients> pq = em.createNamedQuery("Patients.findByEmail", Patients.class);
            pq.setParameter("email", email);
            Patients patient = pq.getSingleResult();
            if (patient != null) {
                patient.setPassword(newPassword);
                em.merge(patient);
                em.getTransaction().commit(); // Commit thay đổi
                return true;
            }
        } catch (Exception ex) {
            em.getTransaction().rollback(); // Rollback nếu lỗi
        }

        return false;
    }

}
