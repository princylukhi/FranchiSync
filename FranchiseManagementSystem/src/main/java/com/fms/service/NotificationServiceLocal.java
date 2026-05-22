/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package com.fms.service;

import com.fms.entity.Notifications;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author Princy Lukhi
 */
@Local
public interface NotificationServiceLocal {
    
      // Generic notification
    public void sendNotification(String email, String subject, String message, String type);

    // Specific use cases (your module)
    public void sendRequestReceivedEmail(String email);
    public void sendCompanyApproval(String email);
    public void sendCompanyRejection(String email);
    public void sendFranchiseApproval(String email);
    public void sendCredentials(String email, String password);

    // Logs
    public List<Notifications> getAllNotifications();
    
    public List<Notifications> getNotificationsByEmail(String email);
}
