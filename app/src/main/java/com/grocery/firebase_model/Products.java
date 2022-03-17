package com.grocery.firebase_model;

public class Products {

    String productName, productDetails, productImage, productId, productCategory,userId;

    public Products() {

    }

    public Products(String productName, String productDetails, String productImage, String productId, String productCategory,String userId) {
        this.productName = productName;
        this.productDetails = productDetails;
        this.productImage = productImage;
        this.productId = productId;
        this.productCategory = productCategory;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
