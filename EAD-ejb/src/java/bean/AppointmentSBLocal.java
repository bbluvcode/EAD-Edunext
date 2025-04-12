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

    public List<Appointments> getAppointmentsByDoctor(int doctorId);

    public List<Appointments> getAppointmentsByDate(Date date);

    public List<String> getBookedTimeSlots(int doctorId, String dateStr);

    public Appointments getAppointment(int id);

    public void booking(Appointments appointments);

    public void cancel(int patientId);

    public String getHtmlTemplateForDoctor(Appointments a);

    public String getHtmlTemplateForPatient(Appointments a);

    public List<Bills> getBills();

    public List<Bills> getBillsByPatient(int id);

}
