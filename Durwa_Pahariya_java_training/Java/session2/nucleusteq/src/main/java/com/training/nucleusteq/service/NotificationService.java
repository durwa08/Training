package com.training.nucleusteq.service;

import com.training.nucleusteq.component.NotificationComponent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationComponent notificationComponent;

    // Constructor Injection ✅
    public NotificationService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    public String triggerNotification() {
        return notificationComponent.sendNotification();
    }
}