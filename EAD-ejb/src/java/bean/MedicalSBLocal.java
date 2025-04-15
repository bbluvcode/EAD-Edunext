/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package bean;

import entities.Appointments;
import entities.MedicalRecords;
import entities.Medicines;
import entities.Prescriptions;
import jakarta.ejb.Local;

import java.util.List;

/**
 *
 * @author Admin
 */
@Local
public interface MedicalSBLocal {

    void createMedicalRecord(Appointments appointment, String symptoms, String diagnosis);

    MedicalRecords getMedicalRecordByAppointmentId(int appointmentId);

    void updateSymptoms(int recordId, String newSymptoms);

    void updateDiagnosis(int recordId, String newDiagnosis);

    List<Prescriptions> listPrescriptions(int recordId);

    List<MedicalRecords> getMedicalHistoryByPatientId(int patientId);

    List<MedicalRecords> searchMedicalHistoryByPatientId(int patientId, String searchQuery);

    String getPatientNameById(int patientId);

    List<Prescriptions> getPrescriptionsByRecordId(int recordId);

    void addPrescription(Prescriptions prescription);
    
    void updatePrescription(Prescriptions prescription);
    void deletePrescription(int prescriptionId);
    Prescriptions getPrescriptionById(int prescriptionId);

    List<Medicines> getAllMedicines();
}
