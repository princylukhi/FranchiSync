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
@Table(name = "franchise_requests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FranchiseRequests.findAll", query = "SELECT f FROM FranchiseRequests f"),
    @NamedQuery(name = "FranchiseRequests.findByFrid", query = "SELECT f FROM FranchiseRequests f WHERE f.frid = :frid"),
    @NamedQuery(name = "FranchiseRequests.findByOwnerName", query = "SELECT f FROM FranchiseRequests f WHERE f.ownerName = :ownerName"),
    @NamedQuery(name = "FranchiseRequests.findByEmail", query = "SELECT f FROM FranchiseRequests f WHERE f.email = :email"),
    @NamedQuery(name = "FranchiseRequests.findByPhone", query = "SELECT f FROM FranchiseRequests f WHERE f.phone = :phone"),
    @NamedQuery(name = "FranchiseRequests.findByStatus", query = "SELECT f FROM FranchiseRequests f WHERE f.status = :status"),
    @NamedQuery(name = "FranchiseRequests.findByRequestDate", query = "SELECT f FROM FranchiseRequests f WHERE f.requestDate = :requestDate"),
    @NamedQuery(name = "FranchiseRequests.findByApprovedDate", query = "SELECT f FROM FranchiseRequests f WHERE f.approvedDate = :approvedDate")})
public class FranchiseRequests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "frid")
    private Integer frid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "owner_name")
    private String ownerName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "request_date")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @Column(name = "approved_date")
    @Temporal(TemporalType.DATE)
    private Date approvedDate;
    @JoinColumn(name = "cid", referencedColumnName = "cid")
    @ManyToOne(optional = false)
    private Companies cid;

    public FranchiseRequests() {
    }

    public FranchiseRequests(Integer frid) {
        this.frid = frid;
    }

    public FranchiseRequests(Integer frid, String ownerName, String email, String phone, String status, Date requestDate) {
        this.frid = frid;
        this.ownerName = ownerName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Integer getFrid() {
        return frid;
    }

    public void setFrid(Integer frid) {
        this.frid = frid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Companies getCid() {
        return cid;
    }

    public void setCid(Companies cid) {
        this.cid = cid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (frid != null ? frid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FranchiseRequests)) {
            return false;
        }
        FranchiseRequests other = (FranchiseRequests) object;
        if ((this.frid == null && other.frid != null) || (this.frid != null && !this.frid.equals(other.frid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.FranchiseRequests[ frid=" + frid + " ]";
    }
    
}
