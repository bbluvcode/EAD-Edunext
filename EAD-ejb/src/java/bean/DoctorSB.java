/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.Appointments;
import entities.Bills;
import entities.Doctors;
import entities.Prescriptions;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author ACER
 */
@Stateless
public class DoctorSB implements DoctorSBLocal {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EADEdunext");
    private EntityManager em = emf.createEntityManager();

    @Override
    public Doctors getDoctor(int id) {
        return em.find(Doctors.class, id);
    }

    @Override
    public List<Doctors> getDoctorsWithoutAdmin() {
        return em.createQuery("SELECT d FROM Doctors d WHERE d.role = false", Doctors.class)
                .getResultList();
    }

    @Override
    public List<Appointments> getAppointments() {
        return em.createNamedQuery("Appointments.findAll", Appointments.class)
                .getResultList();
    }

    @Override
    public List<Appointments> getAppointmentsByDoctorId(int doctorId) {
        return em.createQuery("SELECT a FROM Appointments a WHERE a.doctorID.doctorID = :doctorId", Appointments.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public void createDoctor(Doctors doctor) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(doctor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Create doctor failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateDoctor(Doctors doctor) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(doctor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Update doctor failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Prescriptions getPrescriptionsByApp(int id) {
        return em.createNamedQuery("Prescriptions.getPrescriptionsByApp", Prescriptions.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Bills getBillsByApp(int id) {
        return em.createNamedQuery("Bills.getBillssByApp", Bills.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
