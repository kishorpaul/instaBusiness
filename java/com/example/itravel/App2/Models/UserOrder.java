package com.example.itravel.App2.Models;

public class UserOrder {
    private String phone;

    public UserOrder(){}

    public UserOrder(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
