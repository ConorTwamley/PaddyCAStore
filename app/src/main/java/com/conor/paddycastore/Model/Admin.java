package com.conor.paddycastore.Model;

public class Admin extends User {

    String isStaff;

    public Admin() {
    }

    public Admin(String isStaff) {
        this.isStaff = "true";
    }

    public String getIsStaff() {
        return isStaff;
    }


    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
