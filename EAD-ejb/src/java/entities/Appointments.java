/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "EADAppointments")
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a"),
    @NamedQuery(name = "Appointments.findByAppointmentID", query = "SELECT a FROM Appointments a WHERE a.appointmentID = :appointmentID"),
    @NamedQuery(name = "Appointments.findByAppointmentDate", query = "SELECT a FROM Appointments a WHERE a.appointmentDate = :appointmentDate"),
    @NamedQuery(name = "Appointments.findByAppointmentTime", query = "SELECT a FROM Appointments a WHERE a.appointmentTime = :appointmentTime"),
    @NamedQuery(name = "Appointments.findByStatus", query = "SELECT a FROM Appointments a WHERE a.status = :status"),
    @NamedQuery(name = "Appointments.findByNotes", query = "SELECT a FROM Appointments a WHERE a.notes = :notes")})
public class Appointments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AppointmentID")
    private Integer appointmentID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AppointmentDate")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AppointmentTime")
    @Temporal(TemporalType.TIME)
    private Date appointmentTime;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;
    @Size(max = 500)
    @Column(name = "Notes")
    private String notes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointmentID")
    private List<Bills> billsList;
    @JoinColumn(name = "DoctorID", referencedColumnName = "DoctorID")
    @ManyToOne(optional = false)
    private Doctors doctorID;
    @ManyToOne
    @JoinColumn(name = "PatientID", referencedColumnName = "PatientID")
    private Patients patientID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointmentID")
    private List<MedicalRecords> medicalRecordsList;

    public Appointments() {
    }

    public Appointments(Date appointmentDate, Date appointmentTime, String status, String notes, Doctors doctorID, Patients patientID) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Bills> getBillsList() {
        return billsList;
    }

    public void setBillsList(List<Bills> billsList) {
        this.billsList = billsList;
    }

    public Doctors getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Doctors doctorID) {
        this.doctorID = doctorID;
    }

    public Patients getPatientID() {
        return patientID;
    }

    public void setPatientID(Patients patientID) {
        this.patientID = patientID;
    }

    public List<MedicalRecords> getMedicalRecordsList() {
        return medicalRecordsList;
    }

    public void setMedicalRecordsList(List<MedicalRecords> medicalRecordsList) {
        this.medicalRecordsList = medicalRecordsList;
    }

    @Transient
    public String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(appointmentDate);
    }

    @Transient
    public String formatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(appointmentTime);
    }
}
