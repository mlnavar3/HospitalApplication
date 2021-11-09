package com.example.hospitalapplication;

public class Message {
    private String messageID;
    private String recipientName;
    private String role;
    private String subject;
    private String content;
    private String createdAt;

    public Message(String messageID, String recipientName, String role, String subject, String content, String createdAt) {
        this.messageID = messageID;
        this.recipientName = recipientName;
        this.role = role;
        this.subject = subject;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        return recipientName;
    }
}
