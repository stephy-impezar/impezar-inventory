package com.impezar.file;

import com.impezar.model.DefectInventoryList;
import com.impezar.model.Inventory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class WriteFile {
    public void updateFile(String filename, List<DefectInventoryList> defectList, List<Inventory> validList){
        try(
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        ) {
            writer.flush();

            for (DefectInventoryList elem : defectList){
                writer.write(elem +System.lineSeparator());
            }

            for (Inventory elem : validList){
                writer.write(elem +System.lineSeparator());
            }

        } catch (Exception ex){
            System.out.println("File not found");
            ex.printStackTrace();
        }
    }
}
