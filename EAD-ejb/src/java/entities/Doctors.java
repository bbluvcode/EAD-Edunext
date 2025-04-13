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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ACER
 */
@Entity
@Table(name = "EADDoctors")
@NamedQueries({
    @NamedQuery(name = "Doctors.findAll", query = "SELECT d FROM Doctors d"),
    @NamedQuery(name = "Doctors.findByDoctorID", query = "SELECT d FROM Doctors d WHERE d.doctorID = :doctorID"),
    @NamedQuery(name = "Doctors.findByFullName", query = "SELECT d FROM Doctors d WHERE d.fullName = :fullName"),
    @NamedQuery(name = "Doctors.findByEmail", query = "SELECT d FROM Doctors d WHERE d.email = :email"),
    @NamedQuery(name = "Doctors.findBySpecialization", query = "SELECT d FROM Doctors d WHERE d.specialization = :specialization"),
    @NamedQuery(name = "Doctors.findByPhone", query = "SELECT d FROM Doctors d WHERE d.phone = :phone"),
    @NamedQuery(name = "Doctors.findByDateOfBirth", query = "SELECT d FROM Doctors d WHERE d.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "Doctors.findByPassword", query = "SELECT d FROM Doctors d WHERE d.password = :password"),
    @NamedQuery(name = "Doctors.findByRole", query = "SELECT d FROM Doctors d WHERE d.role = :role")})
public class Doctors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DoctorID")
    private Integer doctorID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FullName")
    private String fullName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Email")
    private String email;
    @Size(max = 100)
    @Column(name = "Specialization")
    private String specialization;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "Phone")
    private String phone;
    @Column(name = "DateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Size(max = 255)
    @Column(name = "Password")
    private String password;
    @Column(name = "Role")
    private Boolean role;

    public Doctors() {
    }

    public Doctors(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Doctors(Integer doctorID, String fullName, String email) {
        this.doctorID = doctorID;
        this.fullName = fullName;
        this.email = email;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    @Transient
    public String formatDOB() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(dateOfBirth);
    }

}
