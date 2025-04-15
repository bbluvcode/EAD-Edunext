/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "EADPrescriptions")
@NamedQueries({
    @NamedQuery(name = "Prescriptions.findAll", query = "SELECT p FROM Prescriptions p"),
    @NamedQuery(name = "Prescriptions.findByPrescriptionID", query = "SELECT p FROM Prescriptions p WHERE p.prescriptionID = :prescriptionID"),
    @NamedQuery(name = "Prescriptions.findByQuantity", query = "SELECT p FROM Prescriptions p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "Prescriptions.findByDosage", query = "SELECT p FROM Prescriptions p WHERE p.dosage = :dosage"),
    @NamedQuery(
            name = "Prescriptions.getMedicinesByRecord",
            query = "SELECT p FROM Prescriptions p WHERE p.recordID.appointmentID.appointmentID = :id"),
    @NamedQuery(
            name = "Prescriptions.getPrescriptionsByApp",
            query = "SELECT p FROM Prescriptions p WHERE p.recordID.appointmentID.appointmentID = :id"),
    @NamedQuery(
            name = "Prescriptions.getPrescriptionsByApp",
            query = "SELECT p FROM Prescriptions p WHERE p.recordID.appointmentID.appointmentID = :id")
})
public class Prescriptions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PrescriptionID")
    private Integer prescriptionID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Quantity")
    private int quantity;
    @Size(max = 100)
    @Column(name = "Dosage")
    private String dosage;
    @Column(name = "Duration")
    private Integer duration;
    @JoinColumn(name = "RecordID", referencedColumnName = "RecordID")
    @ManyToOne(optional = false)
    private MedicalRecords recordID;
    @JoinColumn(name = "MedicineID", referencedColumnName = "MedicineID")
    @ManyToOne(optional = false)
    private Medicines medicineID;

    public Prescriptions() {
    }

    public Prescriptions(int quantity, String dosage, Integer duration, MedicalRecords recordID, Medicines medicineID) {
        this.quantity = quantity;
        this.dosage = dosage;
        this.duration = duration;
        this.recordID = recordID;
        this.medicineID = medicineID;
    }

    public Integer getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(Integer prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public MedicalRecords getRecordID() {
        return recordID;
    }

    public void setRecordID(MedicalRecords recordID) {
        this.recordID = recordID;
    }

    public Medicines getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(Medicines medicineID) {
        this.medicineID = medicineID;
    }
}
