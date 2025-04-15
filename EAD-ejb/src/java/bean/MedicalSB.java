/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.Appointments;
import entities.MedicalRecords;
import entities.Prescriptions;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 *
 * @author Admin
 */
@Stateless
public class MedicalSB implements MedicalSBLocal {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EADEdunext");
    private EntityManager em = emf.createEntityManager();

    @Override
    public void createMedicalRecord(Appointments appointment, String symptoms, String diagnosis) {
        try {
            em.getTransaction().begin();
            MedicalRecords record = new MedicalRecords();
            record.setAppointmentID(appointment);
            record.setSymptoms(symptoms);
            record.setDiagnosis(diagnosis);
            record.setCreatedAt(new java.util.Date());
            em.persist(record);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public MedicalRecords getMedicalRecordByAppointmentId(int appointmentId) {
        return em
                .createQuery("SELECT m FROM MedicalRecords m WHERE m.appointmentID.appointmentID = :appointmentId",
                        MedicalRecords.class)
                .setParameter("appointmentId", appointmentId)
                .getSingleResult();
    }

    @Override
    public void updateSymptoms(int recordId, String newSymptoms) {
        try {
            em.getTransaction().begin();
            MedicalRecords record = em.find(MedicalRecords.class, recordId);
            if (record != null) {
                record.setSymptoms(newSymptoms);
                em.merge(record);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void updateDiagnosis(int recordId, String newDiagnosis) {
        try {
            em.getTransaction().begin();
            MedicalRecords record = em.find(MedicalRecords.class, recordId);
            if (record != null) {
                record.setDiagnosis(newDiagnosis);
                em.merge(record);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Prescriptions> listPrescriptions(int recordId) {
        return em
                .createQuery("SELECT p FROM Prescriptions p WHERE p.medicalRecord.recordID = :recordId",
                        Prescriptions.class)
                .setParameter("recordId", recordId)
                .getResultList();
    }

    @Override
    public List<MedicalRecords> getMedicalHistoryByPatientId(int patientId) {
        return em
                .createQuery("SELECT m FROM MedicalRecords m WHERE m.appointmentID.patientID.patientID = :patientId",
                        MedicalRecords.class)
                .setParameter("patientId", patientId)
                .getResultList();
    }

    @Override
    public List<MedicalRecords> searchMedicalHistoryByPatientId(int patientId, String searchQuery) {
        // return em.createQuery(
        // "SELECT m FROM MedicalRecords m WHERE m.appointmentID.patientID.patientID =
        // :patientId " +
        // "AND (LOWER(m.symptoms) LIKE :searchQuery OR LOWER(m.diagnosis) LIKE
        // :searchQuery OR CAST(m.recordID AS string) LIKE :searchQuery)",
        // MedicalRecords.class)
        // .setParameter("patientId", patientId)
        // .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
        // .getResultList();
        return em.createQuery(
                "SELECT m FROM MedicalRecords m WHERE m.appointmentID.patientID.patientID = :patientId " +
                        "AND (m.symptoms LIKE :searchQuery OR m.diagnosis LIKE :searchQuery)",
                MedicalRecords.class)
                .setParameter("patientId", patientId)
                .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
                .getResultList();
    }
}
