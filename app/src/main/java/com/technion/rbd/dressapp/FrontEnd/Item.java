package com.technion.rbd.dressapp.FrontEnd;

public class Item {
    private String itemId;
    private String itemOwnerId;
    //private String itemColor;
    private String itemGender;
    private String itemCategory;
    private String itemDescription;
    private Measurement itemMeasurement;
    private String itemPicUrl;
    private String itemName;
    private String itemOwnerName;
    private String itemLocation;
    private String itemStatus; // can be Available, Ordered, Delivered,

    public Item(String itemId, String itemOwnerId, /*String itemColor,*/ String itemGender,
                String itemCategory, String itemDescription, Measurement itemMeasurement,
                String itemPicUrl, String itemName, String itemOwnerName, String itemLocation) {
        this.itemId = itemId;
        this.itemOwnerId = itemOwnerId;
        //this.itemColor = itemColor;
        this.itemGender = itemGender;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemMeasurement = itemMeasurement;
        this.itemPicUrl = itemPicUrl;
        this.itemName = itemName;
        this.itemOwnerName = itemOwnerName;
        this.itemLocation = itemLocation;
        this.itemStatus = "Available";
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Item(String itemId, String itemOwnerId, /*String itemColor,*/
                String itemGender, String itemCategory, String itemDescription,
                Measurement itemMeasurement) {

        this.itemId = itemId;
        this.itemOwnerId = itemOwnerId;

        //this.itemColor = itemColor;
        this.itemGender = itemGender;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemMeasurement = itemMeasurement;
        this.itemOwnerName = "No name";
        this.itemLocation = "No location";
        this.itemStatus = "Available";
    }

    public Item(String itemId, String itemOwnerId, /*String itemColor,*/
                String itemGender, String itemCategory, String itemDescription,
                Measurement itemMeasurement, String itemOwnerName, String itemLocation) {

        this.itemId = itemId;
        this.itemOwnerId = itemOwnerId;
        //this.itemColor = itemColor;
        this.itemGender = itemGender;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemMeasurement = itemMeasurement;
        this.itemOwnerName = itemOwnerName;
        this.itemLocation = itemLocation;
        this.itemStatus = "Available";
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemOwnerId='" + itemOwnerId + '\'' +
                //", itemColor='" + itemColor + '\'' +
                ", itemGender='" + itemGender + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemMeasurement=" + itemMeasurement +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public String toString2() {
        return
                itemId +
                        ", " + itemOwnerId +
                        // ", " + itemColor +
                        ", " + itemGender +
                        ", " + itemCategory +
                        ", " + itemDescription
                ;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemOwnerId() {
        return itemOwnerId;
    }

    public void setItemOwnerId(String itemOwnerId) {
        this.itemOwnerId = itemOwnerId;
    }

    public String getItemGender() {
        return itemGender;
    }

    public void setItemGender(String itemGender) {
        this.itemGender = itemGender;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Measurement getItemMeasurement() {
        return itemMeasurement;
    }

    public void setItemMeasurement(Measurement itemMeasurement) {
        this.itemMeasurement = itemMeasurement;
    }

    public String getItemPicUrl() {
        return itemPicUrl;
    }

    public void setItemPicUrl(String itemPicUrl) {
        this.itemPicUrl = itemPicUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemOwnerName() {
        return itemOwnerName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemOwnerName(String itemOwnerName) {
        this.itemOwnerName = itemOwnerName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

}

