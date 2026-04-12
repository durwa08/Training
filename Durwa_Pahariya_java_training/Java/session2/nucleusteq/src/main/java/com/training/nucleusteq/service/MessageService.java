package com.training.nucleusteq.service;

import com.training.nucleusteq.component.ShortMessageFormatter;
import com.training.nucleusteq.component.LongMessageFormatter;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ShortMessageFormatter shortFormatter;
    private final LongMessageFormatter longFormatter;

    // Constructor Injection
    public MessageService(ShortMessageFormatter shortFormatter,
                          LongMessageFormatter longFormatter) {
        this.shortFormatter = shortFormatter;
        this.longFormatter = longFormatter;
    }

    public String getMessage(String type, String topic) {

        if ("SHORT".equalsIgnoreCase(type)) {
            return shortFormatter.format(topic);
        } else {
            return longFormatter.format(topic);
        }
    }
}