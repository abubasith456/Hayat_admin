package com.grocery.firebase_model;

public class Products {

    String productName, productDetails, productImage, productId;

    public Products() {

    }

    public Products(String productName, String productDetails, String productImage, String productId) {
        this.productName = productName;
        this.productDetails = productDetails;
        this.productImage = productImage;
        this.productId = productId;
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
