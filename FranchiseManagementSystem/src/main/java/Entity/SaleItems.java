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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "sale_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaleItems.findAll", query = "SELECT s FROM SaleItems s"),
    @NamedQuery(name = "SaleItems.findBySiid", query = "SELECT s FROM SaleItems s WHERE s.siid = :siid"),
    @NamedQuery(name = "SaleItems.findByQuantity", query = "SELECT s FROM SaleItems s WHERE s.quantity = :quantity"),
    @NamedQuery(name = "SaleItems.findByPrice", query = "SELECT s FROM SaleItems s WHERE s.price = :price")})
public class SaleItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "siid")
    private Integer siid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @ManyToOne(optional = false)
    private Products pid;
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    @ManyToOne(optional = false)
    private Sales sid;

    public SaleItems() {
    }

    public SaleItems(Integer siid) {
        this.siid = siid;
    }

    public SaleItems(Integer siid, int quantity, BigDecimal price) {
        this.siid = siid;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getSiid() {
        return siid;
    }

    public void setSiid(Integer siid) {
        this.siid = siid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Products getPid() {
        return pid;
    }

    public void setPid(Products pid) {
        this.pid = pid;
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
        hash += (siid != null ? siid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleItems)) {
            return false;
        }
        SaleItems other = (SaleItems) object;
        if ((this.siid == null && other.siid != null) || (this.siid != null && !this.siid.equals(other.siid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.SaleItems[ siid=" + siid + " ]";
    }
    
}
