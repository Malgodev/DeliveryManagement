/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import main.Connect;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
    
    public Integer getMonthlyOrder(int month){
        try{
            int parcel_num = 0;
            query = "select count(*) as num_parcel from sending where extract(month from send_date) = " + month;
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
    
    public String getMonthlyPayment(int month){
        try{
            int total_payment = 0;
            query = "select sum(payment) as monthly_revenue " +
                    "from (select payment, extract(month from send_date) as the_month from sending) " +
                    "where the_month = " + month + " " +
                    "group by (the_month)";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                total_payment = res.getInt("monthly_revenue");
            }
            
            
            return normalizeMoney(total_payment);
        } catch(SQLException e){
            return "-1";
        }
    }    
    
    public Integer getMonthlyAvgWeight(int month){
        try{
            float avg_weight = 0;
            query = "select avg(weight) as avgWeight " +
                    "from (select weight, extract(month from send_date) as the_month from parcel natural join sending) " +
                    "where the_month = " + month + " " +
                    "group by (the_month)";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                avg_weight = res.getInt("avgWeight");
            }
            
            return Math.round(avg_weight);
        } catch(SQLException e){
            return -1;
        }
    }
    
    public XYChart.Series getMonthlyParcelPerDay(int month){
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Parcels");
        
        int day_in_month = YearMonth.of(2023, month).lengthOfMonth();

        TreeMap<Integer, Integer> dayMap = new TreeMap<>();
        
        try{
            query = "select extract(day from send_date) as day, count(parcel#) as parcel_num " +
                    "from sending " +
                    "where extract(month from send_date) = " + month + " " +
                    "group by send_date";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                dayMap.put(Integer.parseInt(res.getString("day")), res.getInt("parcel_num"));
            }
        }catch(SQLException e){
            System.out.println("bruh");
        }
        
        for (Integer i = 1; i <= day_in_month; i++){
            int parcel_num = dayMap.getOrDefault(i, 0);
            dataSeries.getData().add(new XYChart.Data(i.toString(), parcel_num));
        }
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
    
    public String normalizeMoney(int amount){
        DecimalFormat df = new DecimalFormat("#.##");

        if (amount >= 1_000_000_000) {
            return df.format(amount / 1_000_000_000) + "b";
        } else if (amount >= 1_000_000) {
            return df.format(amount / 1_000_000) + "m";
        }

        return df.format(amount);
    }
}
