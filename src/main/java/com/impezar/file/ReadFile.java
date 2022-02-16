package com.impezar.file;


import com.impezar.model.DefectInventoryList;
import com.impezar.model.Inventory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    public static final List<DefectInventoryList> defectList =new ArrayList<>();
    public static List<Inventory> validList =new ArrayList<>();



    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public ReadFile(String filename){
        try (
                FileReader fr = new FileReader(filename);
                BufferedReader reader = new BufferedReader(fr);
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                if(!tokens[0].equals("ItemName")){
                    if (isString(tokens[0]) && isStringOnlyNumber(tokens[1]) && isStringOnlyNumber(tokens[2]) && isStringOnlyNumber(tokens[3])){
                        BigDecimal csvQuantity = conversionSringToBigdecimal(tokens[1]);
                        BigDecimal csvCostPerPiece = conversionSringToBigdecimal(tokens[2]);
                        BigDecimal discount = conversionSringToBigdecimal(tokens[3]);
                        //  adding file contents to arraylist
                        validList.add(new Inventory(tokens[0],csvQuantity,csvCostPerPiece,discount));

                    } else {
                        System.out.println(" in "+ANSI_YELLOW +tokens[0]+ANSI_RESET+". So customers cannot able to buy "+tokens[0]+".\n");
                        defectList.add(new DefectInventoryList(tokens[0],tokens[1],tokens[2],tokens[3]));
                    }

                } else {
                    defectList.add(new DefectInventoryList(tokens[0], tokens[1], tokens[2], tokens[3]));
                }

            }



        } catch (IOException ex) {
            System.out.println("invalid inventory path");
            ex.printStackTrace();
        }
    }

    private BigDecimal conversionSringToBigdecimal(String token) {
        token = token.replace(",","");
        return new BigDecimal(token);
    }



    private boolean isStringOnlyNumber(String str) {
        boolean validationCheck = false;
        str = str.replace(",","");
        try {
            new BigDecimal(str);
            validationCheck = true;
        } catch (Exception e){
            System.out.print("\nInvalid inventory data "+ANSI_YELLOW +str+ANSI_RESET+"");
        }

        return validationCheck;
    }

    private boolean isString(String str) {
        return ( (str != null) && (!str.equals("")) );
    }






}
