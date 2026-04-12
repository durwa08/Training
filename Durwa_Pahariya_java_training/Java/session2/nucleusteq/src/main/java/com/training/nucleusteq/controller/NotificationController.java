package com.training.nucleusteq.controller;

import com.training.nucleusteq.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private final NotificationService notificationService;

    // Constructor Injection
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String sendNotification() {
        return notificationService.triggerNotification();
    }
}