package com.impezar.query;

import java.math.BigDecimal;
import java.util.Scanner;

public class Questions {

    Scanner sc = new Scanner(System.in);

    public String askingItem() {
        System.out.print("Enter item name: ");
        return sc.next();
    }


    public BigDecimal askingQuantity() {
        BigDecimal requestedQuantity = null;
        System.out.print("Enter required quantity: ");
        try{
            requestedQuantity =sc.nextBigDecimal();

        } catch(Exception e){
            System.out.println("OOPS... Invalid quantity input... ");
            sc.nextLine();
            askingQuantity();
        }
        return requestedQuantity;
    }


}
