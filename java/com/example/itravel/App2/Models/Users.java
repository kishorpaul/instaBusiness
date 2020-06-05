package com.example.itravel.App2.Models;

public class Users {
    private String uName, fName, email, password, phone,imageUrl;

    public Users(String uName, String fName, String email, String password, String phone, String imageUrl) {
        this.uName = uName;
        this.fName = fName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
