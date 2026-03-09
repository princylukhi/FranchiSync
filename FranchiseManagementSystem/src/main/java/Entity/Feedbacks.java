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
import jakarta.persistence.Lob;
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
@Table(name = "feedbacks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedbacks.findAll", query = "SELECT f FROM Feedbacks f"),
    @NamedQuery(name = "Feedbacks.findByFid", query = "SELECT f FROM Feedbacks f WHERE f.fid = :fid"),
    @NamedQuery(name = "Feedbacks.findByRating", query = "SELECT f FROM Feedbacks f WHERE f.rating = :rating"),
    @NamedQuery(name = "Feedbacks.findByFeedbackDate", query = "SELECT f FROM Feedbacks f WHERE f.feedbackDate = :feedbackDate")})
public class Feedbacks implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fid")
    private Integer fid;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private int rating;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feedback_date")
    @Temporal(TemporalType.DATE)
    private Date feedbackDate;
    @JoinColumn(name = "cid", referencedColumnName = "cid")
    @ManyToOne(optional = false)
    private Companies cid;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne(optional = false)
    private Users uid;

    public Feedbacks() {
    }

    public Feedbacks(Integer fid) {
        this.fid = fid;
    }

    public Feedbacks(Integer fid, String message, int rating, Date feedbackDate) {
        this.fid = fid;
        this.message = message;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public Companies getCid() {
        return cid;
    }

    public void setCid(Companies cid) {
        this.cid = cid;
    }

    public Users getUid() {
        return uid;
    }

    public void setUid(Users uid) {
        this.uid = uid;
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
        if (!(object instanceof Feedbacks)) {
            return false;
        }
        Feedbacks other = (Feedbacks) object;
        if ((this.fid == null && other.fid != null) || (this.fid != null && !this.fid.equals(other.fid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Feedbacks[ fid=" + fid + " ]";
    }
    
}
