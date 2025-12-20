package com.model;

import java.util.Date;

public class Complaint {
    private int complaintID;
    private int subscriberID;
    private String subject;
    private String description;
    private String status;
    private Date createdAt;

    public Complaint() {}

    public Complaint(int complaintID, int subscriberID, String subject, String description, String status, Date createdAt) {
        this.complaintID = complaintID;
        this.subscriberID = subscriberID;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
