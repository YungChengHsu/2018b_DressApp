package com.technion.rbd.dressapp;

public class MenuItems {

    //private int itemImage;
    private String clothesName, bustSize, waistSize, length, ownerName, ownerLocation, itemImageUrl, ownerImage;
    private String itemId;

    public MenuItems(String itemImageUrl, String ownerImage, String clothesName, String bustSize,
                     String waistSize, String length, String ownerName, String ownerLocation, String itemId) {
        this.itemImageUrl = itemImageUrl;
        this.ownerImage = ownerImage;
        this.clothesName = clothesName;
        this.bustSize = bustSize;
        this.waistSize = waistSize;
        this.length = length;
        this.ownerName = ownerName;
        this.ownerLocation = ownerLocation;
        this.itemId = itemId;
    }
//
//    public MenuItems(int itemImage, int ownerImage, String clothesName, String bustSize,
//                     String waistSize, String length, String ownerName, String ownerLocation) {
//        this.itemImage = itemImage;
//        this.ownerImage = ownerImage;
//        this.clothesName = clothesName;
//        this.bustSize = bustSize;
//        this.waistSize = waistSize;
//        this.length = length;
//        this.ownerName = ownerName;
//        this.ownerLocation = ownerLocation;
//    }

//    public int getItemImage() {
//        return itemImage;
//    }

    public String getOwnerImage() {
        return ownerImage;
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

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerLocation() {
        return ownerLocation;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public String getItemId() {
        return itemId;
    }
}
