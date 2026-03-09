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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Maitri Moradiya
 */
@Entity
@Table(name = "franchises")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Franchises.findAll", query = "SELECT f FROM Franchises f"),
    @NamedQuery(name = "Franchises.findByFid", query = "SELECT f FROM Franchises f WHERE f.fid = :fid"),
    @NamedQuery(name = "Franchises.findByStatus", query = "SELECT f FROM Franchises f WHERE f.status = :status"),
    @NamedQuery(name = "Franchises.findByCreatedDate", query = "SELECT f FROM Franchises f WHERE f.createdDate = :createdDate")})
public class Franchises implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fid")
    private Integer fid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @JoinColumn(name = "cid", referencedColumnName = "cid")
    @ManyToOne(optional = false)
    private Companies cid;
    @JoinColumn(name = "owner_user_id", referencedColumnName = "uid")
    @ManyToOne(optional = false)
    private Users ownerUserId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fid")
    private Collection<Branches> branchesCollection;

    public Franchises() {
    }

    public Franchises(Integer fid) {
        this.fid = fid;
    }

    public Franchises(Integer fid, String status, Date createdDate) {
        this.fid = fid;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Companies getCid() {
        return cid;
    }

    public void setCid(Companies cid) {
        this.cid = cid;
    }

    public Users getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Users ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @XmlTransient
    public Collection<Branches> getBranchesCollection() {
        return branchesCollection;
    }

    public void setBranchesCollection(Collection<Branches> branchesCollection) {
        this.branchesCollection = branchesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fid != null ? fid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Franchises)) {
            return false;
        }
        Franchises other = (Franchises) object;
        if ((this.fid == null && other.fid != null) || (this.fid != null && !this.fid.equals(other.fid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Franchises[ fid=" + fid + " ]";
    }
    
}
