package com.technion.rbd.dressapp;

public class MyOrderItems {

    private int itemImage, ownerImage;
    private String clothesName, status, ownerName, ownerLocation; //bustSize, waistSize, length, status;

    public MyOrderItems(int itemImage, int ownerImage, String clothesName, String status, String ownerName, String ownerLocation) {
        this.itemImage = itemImage;
        this.ownerImage = ownerImage;
        this.clothesName = clothesName;
//        this.bustSize = bustSize;
//        this.waistSize = waistSize;
//        this.length = length;
        this.status = status;
        this.ownerName = ownerName;
        this.ownerLocation = ownerLocation;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getClothesName() {
        return clothesName;
    }

    public int getOwnerImage() {
        return ownerImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerLocation() {
        return ownerLocation;
    }

    public String getStatus() { return status;}
}