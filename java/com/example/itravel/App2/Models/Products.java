package com.example.itravel.App2.Models;

public class Products {
    private String pName;
    private String pid;
    private String pPrice;
    private String pDesc;
    private String pImageUrl;
    private String verifyStat;

    public Products(){}

    public Products(String pName, String pid, String pPrice, String pDesc, String pImageUrl, String verifyStat) {
        this.pName = pName;
        this.pid = pid;
        this.pPrice = pPrice;
        this.pDesc = pDesc;
        this.pImageUrl = pImageUrl;
        this.verifyStat = verifyStat;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getpImageUrl() {
        return pImageUrl;
    }

    public void setpImageUrl(String pImageUrl) {
        this.pImageUrl = pImageUrl;
    }

    public String getVerifyStat() {
        return verifyStat;
    }

    public void setVerifyStat(String verifyStat) {
        this.verifyStat = verifyStat;
    }

}
