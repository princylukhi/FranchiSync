/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "payments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p"),
    @NamedQuery(name = "Payments.findByPaid", query = "SELECT p FROM Payments p WHERE p.paid = :paid"),
    @NamedQuery(name = "Payments.findByPaymentMode", query = "SELECT p FROM Payments p WHERE p.paymentMode = :paymentMode"),
    @NamedQuery(name = "Payments.findByAmountPaid", query = "SELECT p FROM Payments p WHERE p.amountPaid = :amountPaid"),
    @NamedQuery(name = "Payments.findByPaymentDate", query = "SELECT p FROM Payments p WHERE p.paymentDate = :paymentDate"),
    @NamedQuery(name = "Payments.findByPaymentStatus", query = "SELECT p FROM Payments p WHERE p.paymentStatus = :paymentStatus")})
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "paid")
    private Integer paid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "payment_mode")
    private String paymentMode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "payment_status")
    private String paymentStatus;
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    @ManyToOne(optional = false)
    private Sales sid;

    public Payments() {
    }

    public Payments(Integer paid) {
        this.paid = paid;
    }

    public Payments(Integer paid, String paymentMode, BigDecimal amountPaid, Date paymentDate, String paymentStatus) {
        this.paid = paid;
        this.paymentMode = paymentMode;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Sales getSid() {
        return sid;
    }

    public void setSid(Sales sid) {
        this.sid = sid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paid != null ? paid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.paid == null && other.paid != null) || (this.paid != null && !this.paid.equals(other.paid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Payments[ paid=" + paid + " ]";
    }
    
}
