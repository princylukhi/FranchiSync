package com.fms.bean;

import com.fms.entity.Notifications;
import com.fms.service.NotificationServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class NotificationBean
implements Serializable {

    @EJB
    private NotificationServiceLocal notificationService;

    @Inject
    private LoginBean loginBean;

    private List<Notifications> notifications;

    @PostConstruct
    public void init() {

        String email =
            loginBean.getLoggedUser().getEmail();

        notifications =
            notificationService.getNotificationsByEmail(email);
    }

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(
            List<Notifications> notifications) {

        this.notifications = notifications;
    }
}