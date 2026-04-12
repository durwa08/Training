package com.training.nucleusteq.component;

import org.springframework.stereotype.Component;

@Component
public class LongMessageFormatter {

    public String format(String topic) {
        return "Detailed information regarding: " + topic +
                ". Please review all the updates carefully.";
    }
}