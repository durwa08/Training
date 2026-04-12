package com.training.nucleusteq.controller;

import com.training.nucleusteq.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    // Constructor Injection
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public String getMessage(@RequestParam String type,
                             @RequestParam String topic) {
        return messageService.getMessage(type, topic);
    }
}