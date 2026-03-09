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
@Table(name = "company_registration_requests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompanyRegistrationRequests.findAll", query = "SELECT c FROM CompanyRegistrationRequests c"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByCrid", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.crid = :crid"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByCompanyName", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByContactPerson", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.contactPerson = :contactPerson"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByEmail", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.email = :email"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByPhone", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.phone = :phone"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByBusinessType", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.businessType = :businessType"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByCity", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.city = :city"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByStatus", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.status = :status"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByRequestDate", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.requestDate = :requestDate"),
    @NamedQuery(name = "CompanyRegistrationRequests.findByApprovedDate", query = "SELECT c FROM CompanyRegistrationRequests c WHERE c.approvedDate = :approvedDate")})
public class CompanyRegistrationRequests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "crid")
    private Integer crid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "company_name")
    private String companyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "contact_person")
    private String contactPerson;
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
    @Size(min = 1, max = 50)
    @Column(name = "business_type")
    private String businessType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "city")
    private String city;
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

    public CompanyRegistrationRequests() {
    }

    public CompanyRegistrationRequests(Integer crid) {
        this.crid = crid;
    }

    public CompanyRegistrationRequests(Integer crid, String companyName, String contactPerson, String email, String phone, String businessType, String city, String status, Date requestDate) {
        this.crid = crid;
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.businessType = businessType;
        this.city = city;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Integer getCrid() {
        return crid;
    }

    public void setCrid(Integer crid) {
        this.crid = crid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (crid != null ? crid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyRegistrationRequests)) {
            return false;
        }
        CompanyRegistrationRequests other = (CompanyRegistrationRequests) object;
        if ((this.crid == null && other.crid != null) || (this.crid != null && !this.crid.equals(other.crid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CompanyRegistrationRequests[ crid=" + crid + " ]";
    }
    
}
