/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "sales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sales.findAll", query = "SELECT s FROM Sales s"),
    @NamedQuery(name = "Sales.findBySid", query = "SELECT s FROM Sales s WHERE s.sid = :sid"),
    @NamedQuery(name = "Sales.findByTotalAmount", query = "SELECT s FROM Sales s WHERE s.totalAmount = :totalAmount"),
    @NamedQuery(name = "Sales.findBySaleDate", query = "SELECT s FROM Sales s WHERE s.saleDate = :saleDate")})
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sid")
    private Integer sid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sale_date")
    @Temporal(TemporalType.DATE)
    private Date saleDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sid")
    private Collection<Payments> paymentsCollection;
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    @ManyToOne(optional = false)
    private Branches bid;
    @JoinColumn(name = "staff_id", referencedColumnName = "uid")
    @ManyToOne(optional = false)
    private Users staffId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sid")
    private Collection<SaleItems> saleItemsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sid")
    private Collection<Invoices> invoicesCollection;

    public Sales() {
    }

    public Sales(Integer sid) {
        this.sid = sid;
    }

    public Sales(Integer sid, BigDecimal totalAmount, Date saleDate) {
        this.sid = sid;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    @XmlTransient
    public Collection<Payments> getPaymentsCollection() {
        return paymentsCollection;
    }

    public void setPaymentsCollection(Collection<Payments> paymentsCollection) {
        this.paymentsCollection = paymentsCollection;
    }

    public Branches getBid() {
        return bid;
    }

    public void setBid(Branches bid) {
        this.bid = bid;
    }

    public Users getStaffId() {
        return staffId;
    }

    public void setStaffId(Users staffId) {
        this.staffId = staffId;
    }

    @XmlTransient
    public Collection<SaleItems> getSaleItemsCollection() {
        return saleItemsCollection;
    }

    public void setSaleItemsCollection(Collection<SaleItems> saleItemsCollection) {
        this.saleItemsCollection = saleItemsCollection;
    }

    @XmlTransient
    public Collection<Invoices> getInvoicesCollection() {
        return invoicesCollection;
    }

    public void setInvoicesCollection(Collection<Invoices> invoicesCollection) {
        this.invoicesCollection = invoicesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sales)) {
            return false;
        }
        Sales other = (Sales) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Sales[ sid=" + sid + " ]";
    }
    
}
