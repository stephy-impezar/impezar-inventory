package com.impezar.model;

import java.math.BigDecimal;

public class ShoppingBag {
    String name;
    BigDecimal quantity;
    BigDecimal costPerPiece;
    BigDecimal discount;
    BigDecimal amount;

    public ShoppingBag(String name, BigDecimal quantity, BigDecimal costPerPiece, BigDecimal discount, BigDecimal amount){
        this.name = name;
        this.quantity = quantity;
        this.costPerPiece = costPerPiece;
        this.discount = discount;
        this.amount = amount;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getCostPerPiece() {
        return costPerPiece;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getAmount() {
        return amount;
    }


}
