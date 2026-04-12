package com.training.nucleusteq.component;

import org.springframework.stereotype.Component;

@Component
public class ShortMessageFormatter {

    public String format(String topic) {
        return "Quick update: " + topic;
    }
}