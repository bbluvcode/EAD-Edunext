/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
@Stateless
public class AppointmentSB implements AppointmentSBLocal {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EADEdunext");
    private EntityManager em = emf.createEntityManager();

    @Override
    public Doctors getDoctor(int id) {
        return em.find(Doctors.class, id);
    }

    @Override
    public Patients getPatient(int id) {
        return em.find(Patients.class, id);
    }

    @Override
    public List<Appointments> getAppointments() {
        return em.createNamedQuery("Appointments.findAll", Appointments.class)
                .getResultList();
    }

    @Override
    public List<Appointments> getAppointmentsByDoctor(int doctorId) {
        return em.createNamedQuery("Appointments.findByDoctor", Appointments.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public List<Appointments> getAppointmentsByDate(Date date) {
        return em.createNamedQuery("Appointments.findByDate", Appointments.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public Appointments getAppointment(int id) {
        return em.find(Appointments.class, id);
    }

    @Override
    public void booking(Appointments appointments) {
        try {
            em.getTransaction().begin();
            em.persist(appointments);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @Override
    public void cancel(int patientId) {
        try {
            em.getTransaction().begin();
            Appointments app = getAppointment(patientId);
            em.remove(app);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Doctors> getDoctorsBySpecialization(String specialization) {
        return em.createNamedQuery("Doctors.findBySpecialization", Doctors.class)
                .setParameter("specialization", specialization)
                .getResultList();
    }

    @Override
    public List<String> getBookedTimeSlots(int doctorId, String dateStr) {
        try {
            // Format ngày
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateStr);

            // Truy vấn danh sách giờ đã đặt (kiểu Date chứa Time)
            List<Date> result = em.createQuery(
                    "SELECT a.appointmentTime FROM Appointments a "
                    + "WHERE a.doctorID.doctorID = :doctorId AND a.appointmentDate = :date", Date.class)
                    .setParameter("doctorId", doctorId)
                    .setParameter("date", date)
                    .getResultList();

            // Format giờ thành chuỗi "HH:mm"
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return result.stream()
                    .map(timeFormat::format)
                    .collect(Collectors.toList());

        } catch (ParseException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
