package com.impezar.invoice;

import com.impezar.file.ReadFile;

import com.impezar.file.WriteFile;
import com.impezar.model.ShoppingBag;
import com.impezar.model.DefectInventoryList;
import com.impezar.model.Inventory;
import com.impezar.query.Questions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Invoice {

    protected int slNo;
    Questions query = new Questions();
    WriteFile write = new WriteFile();

    List<DefectInventoryList> defectList;
    List<Inventory> validList;
    List<ShoppingBag> cart = new ArrayList<>();

    Scanner input = new Scanner(System.in);
    String filename = "ABC_Inventory.csv";

    public Invoice(){
        ReadFile readFile = new ReadFile(filename);
        this.defectList = readFile.defectList;
        this.validList = readFile.validList;
    }

    public void startPurchasing(){
        String reqItem = query.askingItem();
        compareItem(reqItem);
    }

    private void compareItem(String reqItem) {

        Inventory selectedInventory = null;
        for (Inventory inventoryElement: validList) {

            if(inventoryElement.getName().equalsIgnoreCase(reqItem)){
                selectedInventory = inventoryElement;
                int res = inventoryElement.getQuantity().compareTo(new BigDecimal("0"));

                if(res == 0){
                    System.out.println("Out of Stock");
                    continueShopping();
                } else {

                    BigDecimal reqQuantity = query.askingQuantity();
                    BigDecimal n = inventoryElement.getQuantity().subtract(reqQuantity);
                    int val = n.compareTo(new BigDecimal("0"));

                    if(val == 1 || val == 0){   // reqQ is less than or equal to csvQ
                        compareQuantity(selectedInventory, reqItem , reqQuantity);


                    } else {    // reqQ is greater than csvQ
                        System.out.println("\nSorry, We have only "+inventoryElement.getQuantity()+" "+inventoryElement.getName()+" in stock.\n Adding "+inventoryElement.getQuantity()+" "+inventoryElement.getName()+" to your cart\n");
                        reqQuantity = inventoryElement.getQuantity();
                        compareQuantity(selectedInventory, reqItem , reqQuantity);
                    }

                }


            }
        }

        if(selectedInventory == null){
            System.out.println("Invalid Item");
        }

        continueShopping();

    }

    private void compareQuantity(Inventory selectedInventory, String reqItem, BigDecimal reqQuantity) {
        if(compareValues(reqQuantity,new BigDecimal("0"))){
            System.out.println("Please enter a valid quantity!");

        } else if(compareValues(reqQuantity,selectedInventory.getQuantity())){  // req <= csvQ


            BigDecimal quantity;
            int indexCart = 0;
            int indexInventory = validList.indexOf(selectedInventory);
            BigDecimal itemCost;
            String name= selectedInventory.getName();
            BigDecimal costPerPiece = selectedInventory.getCostPerPiece();
            BigDecimal dis = selectedInventory.getDiscount();

            // checking cart list whether item selected before
            ShoppingBag selectedCart = null;
            for (ShoppingBag element: cart) {
                if(element.getName().equalsIgnoreCase(reqItem)){
                    selectedCart = element;
                    indexCart = cart.indexOf(element);

                }
            }



            if (selectedCart != null){
                quantity = reqQuantity.add(selectedCart.getQuantity());
                itemCost = calculateCost(costPerPiece, dis, quantity);
                cart.set(indexCart,new ShoppingBag(name,quantity,costPerPiece,dis,itemCost));

            } else {
                slNo++;
                quantity = reqQuantity;
                itemCost = calculateCost(costPerPiece, dis, quantity);
                cart.add(new ShoppingBag(name,quantity,costPerPiece,dis,itemCost));
            }

// update file data

// finding items balance quantity in inventory after adding item to cart
            BigDecimal balanceQuantity = selectedInventory.getQuantity().subtract(quantity).setScale(2, RoundingMode.HALF_UP);

// update Inventory List
            updateValidList(selectedInventory,balanceQuantity, indexInventory);                     // updating csv file quantity

            displayTrolley();


        }
    }

    private void updateValidList(Inventory selectedInventory, BigDecimal quantity, int index) {
        validList.set(index,new Inventory(selectedInventory.getName(), quantity, selectedInventory.getCostPerPiece(), selectedInventory.getDiscount()));
    }

    public void displayTrolley() {

        if(slNo != 0){
            System.out.format("%4s%10s%16s%12s%14s%14s", "slNo", "Item", "Quantity", "Discount", "CostPerPiece","Amount\n");
        }
        int i = 0;
        for (ShoppingBag element: cart) {
            i++;
            System.out.format("%2s%12s%16s%12s%14s%14s", i, element.getName(), element.getQuantity(), element.getDiscount(), element.getCostPerPiece(), element.getAmount()+"\n");
        }
    }

    private BigDecimal calculateCost(BigDecimal costPerPiece, BigDecimal discount, BigDecimal quantity) {
        BigDecimal cost = null;
        try {
            cost = quantity.multiply(costPerPiece);
            cost = cost.subtract((cost.multiply(discount).divide(new BigDecimal("100"))));
            cost = cost.setScale(2, RoundingMode.HALF_UP);



        } catch (Exception e){
            System.out.println("Amount is null");
        }
        return cost;
    }

    private boolean compareValues(BigDecimal elem1, BigDecimal elem2) {
        int res = elem1.compareTo(elem2);
        boolean val;
        if(res == -1 || res == 0){
            val = true;
        } else {
            val = false;
        }
        return val;
    }

    public void continueShopping() {

        if(slNo != 0){
            System.out.print("\nShopping Menu\n 1. Add items \n 2. Remove item \n 3. Bill Generation \nEnter your choice number: ");

        } else {
            System.out.print("\nShopping Menu\n 1. Add items \n 0. Exit  \nEnter your choice number: ");

        }

        try {

            int ch = input.nextInt();

            switch (ch){

                case 1:
                    startPurchasing();
                    break;

                case 2://remove item
                    if(slNo != 0){
                        updateData();
                    } else {
                        System.out.println("Invalid option. Please provide valid number.");
                    }
                    input.nextLine();
                    continueShopping();

                    break;



                case 3 :
                    if(slNo != 0){
                        //        write the file with updated Quantity.
                        write.updateFile(filename, defectList, validList);

                        System.out.println("Generating Bill");
                        new GenerateBill(cart);
                        input.nextLine();
                    } else {
                        System.out.println("Invalid option. Please provide valid number.");
                        continueShopping();
                    }
                    break;

                case 0:

                    if(slNo != 0){
                        System.out.println("Invalid option. Please provide valid value.");
                        continueShopping();
                    } else {
                        System.out.println("No action required..");
                        input.nextLine();
                    }
                    break;




                default:
                    System.out.println("Invalid Input. Please provide valid value. ");
                    continueShopping();
                    break;
            }

        } catch (Exception e){          // input mismatch exception

            System.out.println("\nOOPS.... Enter valid choice");
            input.nextLine();
            continueShopping();
        }
    }


    public void updateData() {

        System.out.print("To remove from cart, ");
        String itemName = query.askingItem();
        Inventory selectedInventory = null;
        ShoppingBag selectedCart = null;
        int cartIndex = 0;
        int inventoryIndex = 0;
        BigDecimal quantity = null;

        for (ShoppingBag element : cart) {
            if (element.getName().equalsIgnoreCase(itemName)) {
                for (Inventory inventoryElement : validList) {
                    if (inventoryElement.getName().equalsIgnoreCase(itemName)) {
                        selectedCart = element;
                        selectedInventory = inventoryElement;
                        cartIndex = cart.indexOf(element);
                        inventoryIndex = validList.indexOf(inventoryElement);

                    }
                }

            }
        }

        if (selectedCart == null) {
            System.out.println("You haven't selected this item " + itemName);
        } else {
            System.out.println(selectedCart.getQuantity() + " " + selectedCart.getName() + " in your cart.");
            quantity = query.askingQuantity();
            BigDecimal n = selectedInventory.getQuantity().add(selectedCart.getQuantity()).subtract(quantity);
            int compareRes = n.compareTo(new BigDecimal("0"));
//            int compareRes = selectedCart.getQuantity().compareTo(quantity);

            System.out.println(selectedInventory.getQuantity().add(selectedCart.getQuantity()));
            System.out.println(quantity);
            System.out.println(n);

            /*if (compareRes == 1) {
                // update quantity in the cart list
                // less than
                BigDecimal cost = calculateCost(selectedCart.getCostPerPiece(), selectedCart.getDiscount(), quantity);
                cart.set(cartIndex, new ShoppingBag(selectedCart.getName(), quantity, selectedCart.getCostPerPiece(), selectedCart.getDiscount(), cost));


            } else if(compareRes == -1) {
                // remove item from cart
                // equals

                System.out.println(selectedCart.getName() + " removed from cart.");
                cart.remove(cartIndex);
                slNo--;

            } else {
                // Greater
                System.out.println("Entered quantity greater than cart quantity");
                System.out.println(selectedCart.getName() + " removed from cart.");
                cart.remove(cartIndex);
                slNo--;
                quantity = selectedInventory.getQuantity();
            }
*/


            // update inventory list
            BigDecimal remainingQ = selectedCart.getQuantity().subtract(quantity);
            quantity = selectedInventory.getQuantity().add(remainingQ);
            validList.set(inventoryIndex, new Inventory(selectedInventory.getName(), quantity, selectedInventory.getCostPerPiece(), selectedInventory.getDiscount()));
//            write.updateFile(filename, defectList, validList);
            displayTrolley();
        }
    }



}
