package com.conor.paddycastore.StrategyPattern;

public class CreditCardPayment implements PaymentStrategy {

    private String name;
    private String cardNumber;
    private String cvv;
    private String dateOfExpiry;

    public CreditCardPayment(String nameCard, String ccNum, String cvv, String expiryDate){
        this.name=nameCard;
        this.cardNumber=ccNum;
        this.cvv=cvv;
        this.dateOfExpiry=expiryDate;
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
