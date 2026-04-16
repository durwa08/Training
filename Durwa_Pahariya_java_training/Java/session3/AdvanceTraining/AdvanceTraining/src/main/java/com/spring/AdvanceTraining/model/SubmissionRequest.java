package com.spring.AdvanceTraining.model;

public class SubmissionRequest {

    private String data;

    public SubmissionRequest() {
    }

    public SubmissionRequest(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}