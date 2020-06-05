package com.example.itravel.App2.Models;

public class History {
    private String shippedId, shippedDate, shippedTime, shippedUserName, shippedPhone, shippedTotalPrice, shippedState;

    public History() {
    }

    public History(String shippedId, String shippedDate, String shippedTime, String shippedUserName, String shippedPhone, String shippedTotalPrice, String shippedState) {
        this.shippedId = shippedId;
        this.shippedDate = shippedDate;
        this.shippedTime = shippedTime;
        this.shippedUserName = shippedUserName;
        this.shippedPhone = shippedPhone;
        this.shippedTotalPrice = shippedTotalPrice;
        this.shippedState = shippedState;
    }

    public String getShippedId() {
        return shippedId;
    }

    public void setShippedId(String shippedId) {
        this.shippedId = shippedId;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getShippedTime() {
        return shippedTime;
    }

    public void setShippedTime(String shippedTime) {
        this.shippedTime = shippedTime;
    }

    public String getShippedUserName() {
        return shippedUserName;
    }

    public void setShippedUserName(String shippedUserName) {
        this.shippedUserName = shippedUserName;
    }

    public String getShippedPhone() {
        return shippedPhone;
    }

    public void setShippedPhone(String shippedPhone) {
        this.shippedPhone = shippedPhone;
    }

    public String getShippedTotalPrice() {
        return shippedTotalPrice;
    }

    public void setShippedTotalPrice(String shippedTotalPrice) {
        this.shippedTotalPrice = shippedTotalPrice;
    }

    public String getShippedState() {
        return shippedState;
    }

    public void setShippedState(String shippedState) {
        this.shippedState = shippedState;
    }
}