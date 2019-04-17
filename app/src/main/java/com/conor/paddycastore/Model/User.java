package com.conor.paddycastore.Model;

public class User {

    private String username;//Primary key
    private String password;
    private String isStaff;
    private String name;

    public User() {
    }

    public User(String password, String name) {
        this.password = password;
        this.name = name;
        this.isStaff = "false";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}