package com.technion.rbd.dressapp;

public class ProfileItems {

    private String imageUrl;
    private String clothesName, bustSize, waistSize, length, itemId;

    public ProfileItems(String imageUrl, String clothesName, String bustSize, String waistSize, String length, String id) {
        this.imageUrl = imageUrl;
        this.clothesName = clothesName;
        this.bustSize = bustSize;
        this.waistSize = waistSize;
        this.length = length;
        this.itemId = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getClothesName() {
        return clothesName;
    }

    public String getBustSize() {
        return bustSize;
    }

    public String getWaistSize() {
        return waistSize;
    }

    public String getLength() {
        return length;
    }

    public String getItemId() {
        return itemId;
    }


}
