package com.training.nucleusteq.component;

import org.springframework.stereotype.Component;

@Component
public class NotificationComponent {

    // Method to generate notification message
    public String sendNotification(String message) {
        return "Notification sent: " + message;
    }
}