package com.grocery.model.dbCart;

public class CartModel {

    String itemName, itemDetails, itemImage, itemId, itemCategory,userId;

    public CartModel(String itemName, String itemDetails, String itemImage, String itemId, String itemCategory, String userId) {
        this.itemName = itemName;
        this.itemDetails = itemDetails;
        this.itemImage = itemImage;
        this.itemId = itemId;
        this.itemCategory = itemCategory;
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
