package com.brsanthu.googleanalytics.hitdebug;

public class ParserMessage {
    private String messageType;
    private String description;
    private String parameter;

    public String getMessageType() {
        return messageType;
    }

    public ParserMessage setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ParserMessage setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getParameter() {
        return parameter;
    }

    public ParserMessage setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public boolean isInfo() {
        return "info".equalsIgnoreCase(messageType);
    }

    public boolean isWarn() {
        return "warn".equalsIgnoreCase(messageType);
    }

    public boolean isError() {
        return "error".equalsIgnoreCase(messageType);
    }
}
