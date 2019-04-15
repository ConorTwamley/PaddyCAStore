package com.conor.paddycastore.Model;

public class User {

    String username;//Primary key
    String password;
    String isStaff;

    public User() {
    }

    public User(String password) {
        this.password = password;
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

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}