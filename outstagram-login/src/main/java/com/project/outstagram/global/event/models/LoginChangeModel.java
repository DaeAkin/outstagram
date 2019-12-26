package com.project.outstagram.global.event.models;

public class LoginChangeModel {

    private String type;
    private String action;
    private String correlationId;


    public LoginChangeModel(String type, String action, String correlationId) {
        super();
        this.type = type;
        this.action = action;
        this.correlationId = correlationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
