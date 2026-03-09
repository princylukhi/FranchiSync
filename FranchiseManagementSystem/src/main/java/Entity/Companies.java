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
@Table(name = "companies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companies.findAll", query = "SELECT c FROM Companies c"),
    @NamedQuery(name = "Companies.findByCid", query = "SELECT c FROM Companies c WHERE c.cid = :cid"),
    @NamedQuery(name = "Companies.findByCompanyName", query = "SELECT c FROM Companies c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "Companies.findByEmail", query = "SELECT c FROM Companies c WHERE c.email = :email"),
    @NamedQuery(name = "Companies.findByStatus", query = "SELECT c FROM Companies c WHERE c.status = :status"),
    @NamedQuery(name = "Companies.findByCreatedDate", query = "SELECT c FROM Companies c WHERE c.createdDate = :createdDate")})
public class Companies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cid")
    private Integer cid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "company_name")
    private String companyName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<Franchises> franchisesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<Feedbacks> feedbacksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<Users> usersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<Products> productsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<CommissionRules> commissionRulesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cid")
    private Collection<FranchiseRequests> franchiseRequestsCollection;

    public Companies() {
    }

    public Companies(Integer cid) {
        this.cid = cid;
    }

    public Companies(Integer cid, String companyName, String email, String status, Date createdDate) {
        this.cid = cid;
        this.companyName = companyName;
        this.email = email;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @XmlTransient
    public Collection<Franchises> getFranchisesCollection() {
        return franchisesCollection;
    }

    public void setFranchisesCollection(Collection<Franchises> franchisesCollection) {
        this.franchisesCollection = franchisesCollection;
    }

    @XmlTransient
    public Collection<Feedbacks> getFeedbacksCollection() {
        return feedbacksCollection;
    }

    public void setFeedbacksCollection(Collection<Feedbacks> feedbacksCollection) {
        this.feedbacksCollection = feedbacksCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Products> getProductsCollection() {
        return productsCollection;
    }

    public void setProductsCollection(Collection<Products> productsCollection) {
        this.productsCollection = productsCollection;
    }

    @XmlTransient
    public Collection<CommissionRules> getCommissionRulesCollection() {
        return commissionRulesCollection;
    }

    public void setCommissionRulesCollection(Collection<CommissionRules> commissionRulesCollection) {
        this.commissionRulesCollection = commissionRulesCollection;
    }

    @XmlTransient
    public Collection<FranchiseRequests> getFranchiseRequestsCollection() {
        return franchiseRequestsCollection;
    }

    public void setFranchiseRequestsCollection(Collection<FranchiseRequests> franchiseRequestsCollection) {
        this.franchiseRequestsCollection = franchiseRequestsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companies)) {
            return false;
        }
        Companies other = (Companies) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Companies[ cid=" + cid + " ]";
    }
    
}
