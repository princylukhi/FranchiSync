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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "branches")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Branches.findAll", query = "SELECT b FROM Branches b"),
    @NamedQuery(name = "Branches.findByBid", query = "SELECT b FROM Branches b WHERE b.bid = :bid"),
    @NamedQuery(name = "Branches.findByBranchName", query = "SELECT b FROM Branches b WHERE b.branchName = :branchName"),
    @NamedQuery(name = "Branches.findByLocation", query = "SELECT b FROM Branches b WHERE b.location = :location"),
    @NamedQuery(name = "Branches.findByStatus", query = "SELECT b FROM Branches b WHERE b.status = :status")})
public class Branches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bid")
    private Integer bid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "branch_name")
    private String branchName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "fid", referencedColumnName = "fid")
    @ManyToOne(optional = false)
    private Franchises fid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bid")
    private Collection<Inventory> inventoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bid")
    private Collection<Sales> salesCollection;
    @OneToMany(mappedBy = "bid")
    private Collection<Users> usersCollection;

    public Branches() {
    }

    public Branches(Integer bid) {
        this.bid = bid;
    }

    public Branches(Integer bid, String branchName, String location, String status) {
        this.bid = bid;
        this.branchName = branchName;
        this.location = location;
        this.status = status;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Franchises getFid() {
        return fid;
    }

    public void setFid(Franchises fid) {
        this.fid = fid;
    }

    @XmlTransient
    public Collection<Inventory> getInventoryCollection() {
        return inventoryCollection;
    }

    public void setInventoryCollection(Collection<Inventory> inventoryCollection) {
        this.inventoryCollection = inventoryCollection;
    }

    @XmlTransient
    public Collection<Sales> getSalesCollection() {
        return salesCollection;
    }

    public void setSalesCollection(Collection<Sales> salesCollection) {
        this.salesCollection = salesCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bid != null ? bid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branches)) {
            return false;
        }
        Branches other = (Branches) object;
        if ((this.bid == null && other.bid != null) || (this.bid != null && !this.bid.equals(other.bid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Branches[ bid=" + bid + " ]";
    }
    
}
