/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author binhp
 */
public class Database {
    // Enter the query command here

    private String file_addr = "C:\\Users\\binhp\\Documents\\test.txt";

    public Integer getMonthlyOrder(){
        return 20;
    }
    
    public Integer getMonthlyPayment(){
        return 19;
    }    
    
    public Integer getMonthlyAvgWeight(){
        return 18;
    }
    
    public XYChart.Series getMonthlyParcelPerDay(){
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Parcels");
        
        dataSeries.getData().add(new XYChart.Data("1", 12));
        dataSeries.getData().add(new XYChart.Data("2", 38));
        dataSeries.getData().add(new XYChart.Data("3", 41));
        dataSeries.getData().add(new XYChart.Data("4", 25));
        dataSeries.getData().add(new XYChart.Data("5", 9));
        dataSeries.getData().add(new XYChart.Data("6", 30));
        dataSeries.getData().add(new XYChart.Data("7", 29));    
        
        return dataSeries;
    }
    
    public ArrayList<String> getTransportType(){
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Standard");
        arr.add("Saving");
        arr.add("Express");
        arr.add("Instant");
        return arr;
    }
    
    public void insertCustomer(String s){
        System.out.println(s);
        IOFile.Write(file_addr, s + "\n");
    }
}
