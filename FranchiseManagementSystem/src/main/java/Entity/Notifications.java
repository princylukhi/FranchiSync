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
import jakarta.persistence.Lob;
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
@Table(name = "notifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n"),
    @NamedQuery(name = "Notifications.findByNid", query = "SELECT n FROM Notifications n WHERE n.nid = :nid"),
    @NamedQuery(name = "Notifications.findByRecipientEmail", query = "SELECT n FROM Notifications n WHERE n.recipientEmail = :recipientEmail"),
    @NamedQuery(name = "Notifications.findBySubject", query = "SELECT n FROM Notifications n WHERE n.subject = :subject"),
    @NamedQuery(name = "Notifications.findByNotificationType", query = "SELECT n FROM Notifications n WHERE n.notificationType = :notificationType"),
    @NamedQuery(name = "Notifications.findBySentDate", query = "SELECT n FROM Notifications n WHERE n.sentDate = :sentDate")})
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nid")
    private Integer nid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "recipient_email")
    private String recipientEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "notification_type")
    private String notificationType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sent_date")
    @Temporal(TemporalType.DATE)
    private Date sentDate;

    public Notifications() {
    }

    public Notifications(Integer nid) {
        this.nid = nid;
    }

    public Notifications(Integer nid, String recipientEmail, String subject, String message, String notificationType, Date sentDate) {
        this.nid = nid;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
        this.notificationType = notificationType;
        this.sentDate = sentDate;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nid != null ? nid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.nid == null && other.nid != null) || (this.nid != null && !this.nid.equals(other.nid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Notifications[ nid=" + nid + " ]";
    }
    
}
