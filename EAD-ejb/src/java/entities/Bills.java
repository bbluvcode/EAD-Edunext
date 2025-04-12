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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "EADBills")
@NamedQueries({
    @NamedQuery(name = "Bills.findAll", query = "SELECT b FROM Bills b"),
    @NamedQuery(name = "Bills.findByBillID", query = "SELECT b FROM Bills b WHERE b.billID = :billID"),
    @NamedQuery(name = "Bills.findByAmount", query = "SELECT b FROM Bills b WHERE b.amount = :amount"),
    @NamedQuery(name = "Bills.findByPaymentDate", query = "SELECT b FROM Bills b WHERE b.paymentDate = :paymentDate"),
    @NamedQuery(name = "Bills.findByPaymentMethod", query = "SELECT b FROM Bills b WHERE b.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Bills.findByStatus", query = "SELECT b FROM Bills b WHERE b.status = :status"),
    @NamedQuery(
            name = "Bills.findByPatientId",
            query = "SELECT b FROM Bills b WHERE b.appointmentID.patientID.patientID = :id"
    )
})
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BillID")
    private Integer billID;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Amount")
    private BigDecimal amount;
    @Column(name = "PaymentDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Size(max = 50)
    @Column(name = "PaymentMethod")
    private String paymentMethod;
    @Size(max = 50)
    @Column(name = "Status")
    private String status;
    @JoinColumn(name = "AppointmentID", referencedColumnName = "AppointmentID")
    @ManyToOne(optional = false)
    private Appointments appointmentID;

    public Bills() {
    }

    public Integer getBillID() {
        return billID;
    }

    public void setBillID(Integer billID) {
        this.billID = billID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Appointments getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Appointments appointmentID) {
        this.appointmentID = appointmentID;
    }

    @Transient
    public String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(paymentDate);
    }
}
