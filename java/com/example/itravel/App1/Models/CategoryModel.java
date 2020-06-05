package com.example.itravel.App1.Models;

public class CategoryModel {
    int images;
    String desc, title;

    public CategoryModel(int images, String desc, String title) {
        this.images = images;
        this.desc = desc;
        this.title = title;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
