package com.todoapp.ToDoApp.client;

import org.springframework.stereotype.Component;

@Component // make bean (java object)tells Spring to manage this class
public class NotificationServiceClient {

    // this method will send Notifications->prints message instead of real API call
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}