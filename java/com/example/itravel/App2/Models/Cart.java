package com.example.itravel.App2.Models;

public class Cart {
    private String pid, pName,pPrice,pDesc,pDate,pTime,quantity,discount,pImageUrl;

    public Cart(){}

    public Cart(String pid, String pName, String pPrice, String pDesc, String pDate, String pTime, String quantity, String discount, String pImageUrl) {
        this.pid = pid;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pDesc = pDesc;
        this.pDate = pDate;
        this.pTime = pTime;
        this.quantity = quantity;
        this.discount = discount;
        this.pImageUrl = pImageUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpDesc() {
        return pDesc;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getpImageUrl() {
        return pImageUrl;
    }

    public void setpImageUrl(String pImageUrl) {
        this.pImageUrl = pImageUrl;
    }
}
