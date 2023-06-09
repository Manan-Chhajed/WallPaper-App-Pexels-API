package com.example.wallpaperapp;

public class CategoryRVModal {

    private String category;
    private String imgUrl;

    public CategoryRVModal(String category, String imgUrl) {
        this.category = category;
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryIVUrl() {
        return imgUrl;
    }

    public void setCategoryIVUrl(String categoryIVUrl) {
        this.imgUrl = categoryIVUrl;
    }
}
