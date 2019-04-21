package com.conor.paddycastore.Model;

import java.util.List;

public class Request {

    private String username;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<Order> products;

    public Request() {
    }

    public Request(String username, String name, String address, String total, List<Order> products) {
        this.username = username;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = "0";
        this.products = products;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }
}
