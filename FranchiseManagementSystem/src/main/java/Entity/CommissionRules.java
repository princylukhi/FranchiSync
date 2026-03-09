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
@Table(name = "commission_rules")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommissionRules.findAll", query = "SELECT c FROM CommissionRules c"),
    @NamedQuery(name = "CommissionRules.findByCoid", query = "SELECT c FROM CommissionRules c WHERE c.coid = :coid"),
    @NamedQuery(name = "CommissionRules.findByCoPercentage", query = "SELECT c FROM CommissionRules c WHERE c.coPercentage = :coPercentage")})
public class CommissionRules implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "coid")
    private Integer coid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "co_percentage")
    private BigDecimal coPercentage;
    @JoinColumn(name = "cid", referencedColumnName = "cid")
    @ManyToOne(optional = false)
    private Companies cid;

    public CommissionRules() {
    }

    public CommissionRules(Integer coid) {
        this.coid = coid;
    }

    public CommissionRules(Integer coid, BigDecimal coPercentage) {
        this.coid = coid;
        this.coPercentage = coPercentage;
    }

    public Integer getCoid() {
        return coid;
    }

    public void setCoid(Integer coid) {
        this.coid = coid;
    }

    public BigDecimal getCoPercentage() {
        return coPercentage;
    }

    public void setCoPercentage(BigDecimal coPercentage) {
        this.coPercentage = coPercentage;
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
        hash += (coid != null ? coid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommissionRules)) {
            return false;
        }
        CommissionRules other = (CommissionRules) object;
        if ((this.coid == null && other.coid != null) || (this.coid != null && !this.coid.equals(other.coid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CommissionRules[ coid=" + coid + " ]";
    }
    
}
