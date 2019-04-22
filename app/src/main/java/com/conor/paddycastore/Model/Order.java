package com.conor.paddycastore.Model;

public class Order {

    private String ProductName;
    private String Quantity;
    private String Price;
    private String image;

    public Order() {
    }

    public Order(String productName, String quantity, String price, String image) {
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        this.image = image;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
