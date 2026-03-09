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

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findByInid", query = "SELECT i FROM Inventory i WHERE i.inid = :inid"),
    @NamedQuery(name = "Inventory.findByQuantity", query = "SELECT i FROM Inventory i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "Inventory.findByMinThreshold", query = "SELECT i FROM Inventory i WHERE i.minThreshold = :minThreshold")})
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "inid")
    private Integer inid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "min_threshold")
    private int minThreshold;
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    @ManyToOne(optional = false)
    private Branches bid;
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @ManyToOne(optional = false)
    private Products pid;

    public Inventory() {
    }

    public Inventory(Integer inid) {
        this.inid = inid;
    }

    public Inventory(Integer inid, int quantity, int minThreshold) {
        this.inid = inid;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
    }

    public Integer getInid() {
        return inid;
    }

    public void setInid(Integer inid) {
        this.inid = inid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(int minThreshold) {
        this.minThreshold = minThreshold;
    }

    public Branches getBid() {
        return bid;
    }

    public void setBid(Branches bid) {
        this.bid = bid;
    }

    public Products getPid() {
        return pid;
    }

    public void setPid(Products pid) {
        this.pid = pid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inid != null ? inid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.inid == null && other.inid != null) || (this.inid != null && !this.inid.equals(other.inid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Inventory[ inid=" + inid + " ]";
    }
    
}
