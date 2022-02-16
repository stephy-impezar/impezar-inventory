package com.impezar.model;

public class DefectInventoryList {
    private String name;
    private String quantity;
    private String costPerPiece;
    private String discount;


    public DefectInventoryList(String name, String quantity, String costPerPiece, String discount){
        this.name = name;
        this.quantity = quantity;
        this.costPerPiece = costPerPiece;
        this.discount = discount;
    }

    public String toString(){
        return name +","+ quantity+","+ costPerPiece +","+discount;
    }

}
