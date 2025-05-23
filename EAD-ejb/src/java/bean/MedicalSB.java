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
        return em.createQuery("SELECT m FROM MedicalRecords m WHERE m.appointmentID.appointmentID = :appointmentId", MedicalRecords.class)
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
        return em.createQuery("SELECT p FROM Prescriptions p WHERE p.medicalRecord.recordID = :recordId", Prescriptions.class)
                .setParameter("recordId", recordId)
                .getResultList();
    }

    @Override
    public Prescriptions getPrescriptionById(int prescriptionId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addPrescription(Prescriptions prescription) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updatePrescription(Prescriptions prescription) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletePrescription(int prescriptionID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Medicines> getAllMedicines() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getPatientNameById(int patientId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MedicalRecords> getMedicalHistoryByPatientId(int patientId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MedicalRecords> searchMedicalHistoryByPatientId(int patientId, String search) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
