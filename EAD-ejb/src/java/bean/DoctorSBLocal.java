/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package bean;

import entities.Appointments;
import entities.Doctors;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author ACER
 */
@Local
public interface DoctorSBLocal {

    public Doctors getDoctor(int id);

    public List<Doctors> getDoctorsWithoutAdmin();

    public List<Appointments> getAppointments();

    public List<Appointments> getAppointmentsByDoctorId(int doctorId);

    public void createDoctor(Doctors doctors);

    public void updateDoctor(Doctors doctors);
}
