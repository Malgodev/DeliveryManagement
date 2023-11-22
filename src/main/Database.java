/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import main.Connect;

import java.sql.*;

/**
 *
 * @author binhp
 */
public class Database{
    // Enter the query command here

    private String file_addr = "C:\\Users\\binhp\\Documents\\test.txt";
    
    private Connection connection = Connect.getConnection();
    private PreparedStatement statement;
    private Statement state;
    String query;
    ResultSet res;
    
    public Database() throws SQLException{
    }
    
    public Integer getMonthlyOrder(){
        try{
            int parcel_num = 0;
            query = "select count(*) as num_parcel from sending where extract(month from send_date) = 9";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            while(res.next()){
                parcel_num = res.getInt("num_parcel");
            }
            return parcel_num;
            
        }catch(SQLException e){
            System.out.println("bruh");
            return 0;
        }
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
        
//        try{
//            res = statement.executeQuery("");
//        }catch(SQLException e){
//            
//        }
        
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
