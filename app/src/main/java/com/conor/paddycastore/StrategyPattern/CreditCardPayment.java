package com.conor.paddycastore.StrategyPattern;

import com.conor.paddycastore.Model.Order;

import java.util.List;

public class CreditCardPayment implements PaymentStrategy {

    String name;
    String cardNumber;
    String cvv;
    String dateOfExpiryMonth;
    String dateOfExpiryYear;
    String total;
    List<Order> cart;

    public CreditCardPayment(String name, String cardNumber, String cvv, String dateOfExpiryMonth, String dateOfExpiryYear, List<Order> cart, String total) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dateOfExpiryMonth = dateOfExpiryMonth;
        this.dateOfExpiryYear = dateOfExpiryYear;
        this.cart =  cart;
        this.total = total;
    }

    @Override
    public void pay(String amount) {
        System.out.println(amount +" paid with credit/debit card");
    }
//
//    @Override
//    public boolean pay(String amount) {
//        return true; // if payment goes through
//    }
}
