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
import java.util.Date;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "invoices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i"),
    @NamedQuery(name = "Invoices.findByInvid", query = "SELECT i FROM Invoices i WHERE i.invid = :invid"),
    @NamedQuery(name = "Invoices.findByInvoiceNumber", query = "SELECT i FROM Invoices i WHERE i.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "Invoices.findByInvoiceDate", query = "SELECT i FROM Invoices i WHERE i.invoiceDate = :invoiceDate")})
public class Invoices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "invid")
    private Integer invid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    @ManyToOne(optional = false)
    private Sales sid;

    public Invoices() {
    }

    public Invoices(Integer invid) {
        this.invid = invid;
    }

    public Invoices(Integer invid, String invoiceNumber, Date invoiceDate) {
        this.invid = invid;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
    }

    public Integer getInvid() {
        return invid;
    }

    public void setInvid(Integer invid) {
        this.invid = invid;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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
        hash += (invid != null ? invid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoices)) {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.invid == null && other.invid != null) || (this.invid != null && !this.invid.equals(other.invid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Invoices[ invid=" + invid + " ]";
    }
    
}
