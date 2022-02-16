package com.impezar.model;

import java.math.BigDecimal;

public class Inventory {
    String name;
    BigDecimal quantity;
    BigDecimal costPerPiece;
    BigDecimal discount;




    public Inventory(String name, BigDecimal quantity, BigDecimal costPerPiece, BigDecimal discount) {
        this.name = name;
        this.quantity = quantity;
        this.costPerPiece = costPerPiece;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostPerPiece() {
        return costPerPiece;
    }

    public void setCostPerPiece(BigDecimal costPerPiece) {
        this.costPerPiece = costPerPiece;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }



    public String toString(){
        return name +","+ quantity+","+ costPerPiece +","+discount;
    }



}
