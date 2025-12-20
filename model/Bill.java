package com.model;

import java.util.Date;

public class Bill {
    private int billID;
    private int subscriberID;
    private int serviceID;
    private double amount;
    private Date date;
    private String type;
    private String reference;
    private String status;

    public Bill() {}

    public Bill(int billID, int subscriberID, int serviceID, double amount, Date date, String type, String reference, String status) {
        this.billID = billID;
        this.subscriberID = subscriberID;
        this.serviceID = serviceID;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.reference = reference;
        this.status = status;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
