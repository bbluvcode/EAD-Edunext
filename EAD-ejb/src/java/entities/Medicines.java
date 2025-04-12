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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "EADMedicines")
@NamedQueries({
    @NamedQuery(name = "Medicines.findAll", query = "SELECT m FROM Medicines m"),
    @NamedQuery(name = "Medicines.findByMedicineID", query = "SELECT m FROM Medicines m WHERE m.medicineID = :medicineID"),
    @NamedQuery(name = "Medicines.findByMedicineName", query = "SELECT m FROM Medicines m WHERE m.medicineName = :medicineName"),
    @NamedQuery(name = "Medicines.findByUnit", query = "SELECT m FROM Medicines m WHERE m.unit = :unit"),
    @NamedQuery(name = "Medicines.findByPrice", query = "SELECT m FROM Medicines m WHERE m.price = :price")})
public class Medicines implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MedicineID")
    private Integer medicineID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MedicineName")
    private String medicineName;
    @Size(max = 20)
    @Column(name = "Unit")
    private String unit;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Price")
    private BigDecimal price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicineID")
    private List<Prescriptions> prescriptionsList;

    public Medicines() {
    }
    
    public Integer getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Prescriptions> getPrescriptionsList() {
        return prescriptionsList;
    }

    public void setPrescriptionsList(List<Prescriptions> prescriptionsList) {
        this.prescriptionsList = prescriptionsList;
    }

}
