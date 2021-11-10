package com.example.hospitalapplication;

public class StaffMessage {
    private String messageID;
    private String recipientName;
    private Patient sender;
    private String subject;
    private String content;
    private String createdAt;

    public StaffMessage(String messageID, String recipientName, String subject, String content, String createdAt) {
        this.messageID = messageID;
        this.recipientName = recipientName;
        this.subject = subject;
        this.content = content;
        this.createdAt = createdAt;
    }

    public StaffMessage(Patient senderName, String subject, String content, String createdAt){
        this.sender = senderName;
        this.subject = subject;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Patient getSender() {
        return sender;
    }

    public void setSenderName(Patient sender) {
        this.sender = sender;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Patient getRecipientName() {
        return sender;
    }

    public void setRecipientName(Patient recipientName) {
        this.sender = recipientName;
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
        return sender.getFullName();
    }
}
