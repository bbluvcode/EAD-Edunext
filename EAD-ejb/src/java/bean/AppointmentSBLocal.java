/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package bean;

import jakarta.ejb.Local;
import entities.*;
import java.util.*;

/**
 *
 * @author Admin
 */
@Local
public interface AppointmentSBLocal {

    public Doctors getDoctor(int id);

    public Patients getPatient(int id);

    public List<Doctors> getDoctorsBySpecialization(String specialization);

    public List<Appointments> getAppointments();
    
    public List<Appointments> getAppsByPatients(int id);

    public List<Appointments> getAppointmentsByDoctor(int doctorId);

    public List<Appointments> getAppointmentsByDate(Date date);

    public List<String> getBookedTimeSlots(int doctorId, String dateStr);

    public Appointments getAppointment(int id);

    public void booking(Appointments appointments);
    
    public void updateBooking(Appointments appointments);

    public void cancel(Appointments appointments);

    public String getHtmlTemplateForDoctor(Appointments a);

    public String getHtmlTemplateForPatient(Appointments a);
    
    public String getHtmlTemplateForCanceledAppointment(Appointments a);

    public List<Bills> getBills();

    public List<Bills> getBillsByPatient(int id);
    
    public Bills getOneBill(int appointmentID);
    
    public List<Medicines> getMedicines();
    
    public List<Prescriptions> getMedicinesByRecord(int id);
    
    public Medicines getOneMedicines(int id);
    
    public MedicalRecords getOneMedicalRecords(int id);
    
    public int getRecordID(int appointmentID);
    
    public List<Prescriptions> getPrescriptions();
    
    public void addPrescriptions(Prescriptions p);
    
    public Bills getBillDetail(int id);
    
    public List<PrescriptionDTO> getPrescriptionsByApp(int id);

}
