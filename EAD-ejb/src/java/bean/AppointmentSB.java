/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package bean;

import entities.*;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import util.MailUtil;

/**
 *
 * @author Admin
 */
@Stateless
public class AppointmentSB implements AppointmentSBLocal {

    private MailUtil mailUtil;
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

            String emailContent = getHtmlTemplateForPatient(appointments);
            try {
                mailUtil.sendEmail(appointments.getPatientID().getEmail(), "Xác nhận đặt lịch khám", emailContent);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            String doctorEmailContent = getHtmlTemplateForDoctor(appointments);
            try {
                mailUtil.sendEmail(appointments.getDoctorID().getEmail(), "Bạn có lịch khám mới", doctorEmailContent);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

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
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateStr);

            List<Date> result = em.createQuery(
                    "SELECT a.appointmentTime FROM Appointments a "
                    + "WHERE a.doctorID.doctorID = :doctorId AND a.appointmentDate = :date", Date.class)
                    .setParameter("doctorId", doctorId)
                    .setParameter("date", date)
                    .getResultList();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return result.stream()
                    .map(timeFormat::format)
                    .collect(Collectors.toList());

        } catch (ParseException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Bills> getBills() {
        return em.createNamedQuery("Bills.findAll", Bills.class)
                .getResultList();
    }

    @Override
    public List<Bills> getBillsByPatient(int id) {
        return em.createNamedQuery("Bills.findByPatientId", Bills.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public String getHtmlTemplateForDoctor(Appointments a) {
        String doctorName = a.getDoctorID().getFullName();
        String patientName = a.getPatientID().getFullName();
        String date = a.formatDate();
        String time = a.formatTime();

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #fff7e6; padding: 20px; }"
                + ".container { background-color: #ffffff; border-radius: 10px; padding: 30px; border-left: 5px solid #007bff; }"
                + "h2 { color: #007bff; }"
                + "p { font-size: 16px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h2>Bạn có một lịch hẹn mới</h2>"
                + "<p>Xin chào Bác sĩ <strong>" + doctorName + "</strong>,</p>"
                + "<p>Bạn có lịch hẹn mới với bệnh nhân <strong>" + patientName + "</strong>:</p>"
                + "<ul>"
                + "<li><strong>Ngày khám:</strong> " + date + "</li>"
                + "<li><strong>Thời gian:</strong> " + time + "</li>"
                + "</ul>"
                + "<p>Vui lòng chuẩn bị đầy đủ để khám đúng giờ.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public String getHtmlTemplateForPatient(Appointments a) {
        String patientName = a.getPatientID().getFullName();
        String doctorName = a.getDoctorID().getFullName();
        String department = a.getDoctorID().getSpecialization();
        String date = a.formatDate();
        String time = a.formatTime();

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }"
                + ".container { background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }"
                + "h2 { color: #007bff; }"
                + "p { font-size: 16px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h2>Xác nhận đặt lịch khám thành công</h2>"
                + "<p>Xin chào <strong>" + patientName + "</strong>,</p>"
                + "<p>Bạn đã đặt lịch khám thành công với thông tin sau:</p>"
                + "<ul>"
                + "<li><strong>Bác sĩ:</strong> " + doctorName + " (" + department + ")</li>"
                + "<li><strong>Ngày khám:</strong> " + date + "</li>"
                + "<li><strong>Thời gian:</strong> " + time + "</li>"
                + "</ul>"
                + "<p>Vui lòng đến đúng giờ và mang theo giấy tờ cần thiết.</p>"
                + "<p>Cảm ơn bạn đã tin tưởng bệnh viện của chúng tôi!</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public List<Medicines> getMedicines() {
        return em.createNamedQuery("Medicines.findAll", Medicines.class)
                .getResultList();
    }

    @Override
    public Medicines getOneMedicines(int id) {
        return em.find(Medicines.class, id);
    }

    @Override
    public MedicalRecords getOneMedicalRecords(int id) {
        return em.find(MedicalRecords.class, id);
    }

    @Override
    public List<Prescriptions> getPrescriptions() {
        return em.createNamedQuery("Prescriptions.findAll", Prescriptions.class)
                .getResultList();
    }

    @Override
    public void addPrescriptions(Prescriptions p) {
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.flush();
            em.refresh(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @Override
    public int getRecordID(int appointmentID) {
        MedicalRecords mr = em.createNamedQuery("MedicalRecords.findOneRecord", MedicalRecords.class)
                .setParameter("appointmentID", appointmentID)
                .getSingleResult();
        return mr.getRecordID();

    }

    @Override
    public Bills getOneBill(int appointmentID) {
        Bills bill = em.createNamedQuery("Bills.getOneBill", Bills.class)
                .setParameter("appointmentID", appointmentID)
                .getSingleResult();
        if (bill != null) {
            em.refresh(bill);
        }
        return bill;
    }

    @Override
    public List<Prescriptions> getMedicinesByRecord(int id) {
        return em.createNamedQuery("Prescriptions.getMedicinesByRecord", Prescriptions.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Bills getBillDetail(int id) {
        return em.find(Bills.class, id);
    }

    @Override
    public List<Prescriptions> getPrescriptionsByApp(int id) {
        return em.createNamedQuery("Prescriptions.getPrescriptionsByApp", Prescriptions.class)
                .setParameter("id", id)
                .getResultList();
    }
}
