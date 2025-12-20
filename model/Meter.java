package com.model;

import java.util.Date;

public class Meter {
    private int meterID;
    private String name;
    private String description;
    private String category;
    private double priceOrValue;
    private String status;
    private Date createdAt;

    public Meter() {}

    public Meter(int meterID, String name, String description, String category, double priceOrValue, String status, Date createdAt) {
        this.meterID = meterID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.priceOrValue = priceOrValue;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getMeterID() {
        return meterID;
    }

    public void setMeterID(int meterID) {
        this.meterID = meterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPriceOrValue() {
        return priceOrValue;
    }

    public void setPriceOrValue(double priceOrValue) {
        this.priceOrValue = priceOrValue;
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
