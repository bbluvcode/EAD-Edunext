/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.Appointments;
import entities.MedicalRecords;
import entities.Medicines;
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
        return em.createQuery(
                "SELECT m FROM MedicalRecords m WHERE m.appointmentID.patientID.patientID = :patientId " +
                        "AND (LOWER(m.symptoms) LIKE :searchQuery OR LOWER(m.diagnosis) LIKE :searchQuery)",
                MedicalRecords.class)
                .setParameter("patientId", patientId)
                .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
                .getResultList();
    }

    @Override
    public String getPatientNameById(int patientId) {
        return em.createQuery(
                "SELECT p.fullName FROM Patients p WHERE p.patientID = :patientId", String.class)
                .setParameter("patientId", patientId)
                .getSingleResult();
    }

    @Override
    public List<Prescriptions> getPrescriptionsByRecordId(int recordId) {
        return em.createQuery(
                "SELECT p FROM Prescriptions p WHERE p.recordID.recordID = :recordId", Prescriptions.class)
                .setParameter("recordId", recordId)
                .getResultList();
    }

    @Override
    public void addPrescription(Prescriptions prescription) {
        try {
            em.getTransaction().begin();
            if (prescription != null) {
                em.persist(prescription);
            } else {
                throw new IllegalArgumentException("Prescription add is null.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error deleting prescription add");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updatePrescription(Prescriptions prescription) {
        try {
            em.getTransaction().begin();
            if (prescription != null) {
                em.merge(prescription);
            } else {
                throw new IllegalArgumentException("Prescription update is null.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error deleting prescription update");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deletePrescription(int prescriptionId) {
        try {
            em.getTransaction().begin();
            Prescriptions prescription = em.find(Prescriptions.class, prescriptionId);
            if (prescription != null) {
                System.out.println("Deleting prescription with ID: " + prescriptionId);
                em.remove(prescription);
            } else {
                throw new IllegalArgumentException("Prescription with ID " + prescriptionId + " not found.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error deleting prescription with ID: " + prescriptionId);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Prescriptions getPrescriptionById(int prescriptionId) {
        return em.find(Prescriptions.class, prescriptionId);
    }

    @Override
    public List<Medicines> getAllMedicines() {
        return em.createQuery("SELECT m FROM Medicines m", Medicines.class).getResultList();
    }
}
