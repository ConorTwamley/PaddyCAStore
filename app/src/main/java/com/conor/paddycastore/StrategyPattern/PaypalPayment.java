package com.conor.paddycastore.StrategyPattern;


import com.conor.paddycastore.Model.Order;

import java.util.List;

public class PaypalPayment implements PaymentStrategy {

    String emailId;
    String password;
    List<Order> key;
    String name;

    public PaypalPayment(String email, String pwd, List<Order> key, String name){
        this.emailId=email;
        this.password=pwd;
        this.key = key;
        this.name = name;
    }

    @Override
    public void pay(String amount) {
        System.out.println(amount + " paid using Paypal.");
    }

//    @Override
//    public boolean pay(String amount) {
//        return true; // if payment goes through
//    }

}
