package com.conor.paddycastore.Model;

public class Rating {

    private String userName,productId,rateValue,comment; //userName is both PK and a value

    public Rating() {
    }

    public Rating(String userName, String productId, String rateValue, String comment) {
        this.userName = userName;
        this.productId = productId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
