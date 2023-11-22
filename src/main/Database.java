/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import main.Connect;
import dashboard.ManageController;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.PieChart;


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
            dataSeries.getData().add(new XYChart.Data(String.valueOf(i), parcel_num));
        }
//        if (dayMap.isEmpty()){dataSeries.getData().add(new XYChart.Data("0", 0));}
        return dataSeries;
    }
    
    public ArrayList<String> getTransportType(){
        ArrayList<String> arr = new ArrayList<>();
        
        try{
            query = "select title from transport";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                arr.add(res.getString("title"));
            }
        } catch(SQLException e){
        }
        
        return arr;
    }
    
    public void createParcel(ArrayList<String> snd_info, ArrayList<String> rcv_info, ArrayList<String> parcel_info){
        LocalDate currentDate = LocalDate.now();
        
        int snd_id, rcv_id, parcel_id;
        
        snd_id = checkExist(snd_info.get(0), snd_info.get(1));
        if (snd_id == -1) snd_id = insertCustomer(snd_info);
        else updateCustomer(snd_info, snd_id);

        rcv_id = checkExist(rcv_info.get(0), rcv_info.get(1));
        if (rcv_id == -1) rcv_id = insertCustomer(rcv_info);      
        else updateCustomer(snd_info, rcv_id);

        parcel_id = getParcelID();
        insertParcel(parcel_info);
        
        try {
            int custID = getSendingID();
            query =  "insert into sending "
                    + "(sending#,send_date,sender#,recipient#,parcel#) values "
                    + "(?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(query);
                        
            statement.setInt(1, custID);  
            statement.setDate(2, Date.valueOf(currentDate));
            statement.setInt(3, snd_id);
            statement.setInt(4, rcv_id);
            statement.setInt(5, parcel_id);   
            
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } 
    }
    
    public void updateCustomer(ArrayList<String> arr_s, int custID){
        try {
            query =  "update customer set "
                    + "zip# = ?, address = ?, email = ? "
                    + "where customer# = ?";

            statement = connection.prepareStatement(query);
                        
            statement.setInt(1, Integer.parseInt(arr_s.get(4)));  
            statement.setString(2, arr_s.get(3));
            statement.setString(3, arr_s.get(2));
            statement.setInt(4, custID);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }      
    }
    
    public int insertCustomer(ArrayList<String> arr_s){
        try {
            int custID = getCustID();
            query =  "insert into customer "
                    + "(customer#, full_name, phone#, zip#, address, bank#, bank_account, email) values "
                    + "(?, ?, ?, ?, ?, null, null, ?)";

            statement = connection.prepareStatement(query);
                        
            statement.setInt(1, custID);  
            statement.setString(2, arr_s.get(0));
            statement.setString(3, arr_s.get(1));
            statement.setString(4, arr_s.get(4));
            statement.setString(5, arr_s.get(3));   
            statement.setString(6, arr_s.get(2));
            
            statement.executeUpdate();
            return custID;
        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        }        
    }
    
    public int insertParcel(ArrayList<String> arr_s){
        try {
            int parcelID = getParcelID();
            query =  "insert into parcel "
                    + "(parcel#, weight, transport#, title, note, COD, COD_status, status) values "
                    + "(?, ?, ?, ?, ?, ?, 0, 0)";

            statement = connection.prepareStatement(query);
                        
            statement.setInt(1, parcelID);  
            statement.setInt(2, Integer.parseInt(arr_s.get(1)));
            statement.setInt(3, Integer.parseInt(arr_s.get(4)));
            statement.setString(4, arr_s.get(0));
            statement.setString(5, arr_s.get(2));   
            if ("".equals(arr_s.get(3))) statement.setNull(6, java.sql.Types.NULL);
            else statement.setInt(6, Integer.parseInt(arr_s.get(3)));
            
            statement.executeUpdate();
            return parcelID;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("bruh");
            return -1;
        }        
    }    
    
    public Integer getParcelID(){
        try{
            Integer cust_num = 0;
            query = "select count(*) as parcel_num from parcel";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                cust_num = res.getInt("parcel_num");
            }
            
            return cust_num;
        } catch(SQLException e){
            return -1;
        }        
    }
    
    public Integer getSendingID(){
        try{
            Integer sending_id = 0;
            query = "select count(*) as sending_num from sending";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                sending_id = res.getInt("sending_num");
            }
            
            return sending_id;
        } catch(SQLException e){
            return -1;
        }        
    }    
    
    public Integer getCustID(){
        try{            
            Integer cust_num = 0;
            query = "select count(*) as cust_num from customer";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();        
            while(res.next()){
                cust_num = res.getInt("cust_num");
            }
            
            return cust_num;
        } catch(SQLException e){
            return 0;
        }    
    }
    
    public Integer checkExist(String name, String phone){
        try{
            Integer cust_id = -1;
            query = "select customer# from customer " +
                    "where full_name = ? and phone# = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);  
            statement.setString(2, phone);
            res = statement.executeQuery();       
            
            while(res.next()){
                cust_id = res.getInt("customer#");
            }            
            
            return cust_id; 
        }catch(SQLException e){
            return 0;
        }    
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
    
    public TreeMap<String, Integer> getParcelType(){
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        
        try{
            query = "select title, revenue " +
                    "from transport natural join (select transport#, sum(payment)as revenue " +
                    "from parcel natural join sending " +
                    "group by transport#)";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            while(res.next()){
                treeMap.put(res.getString("title"), res.getInt("revenue"));
            }            
        }catch(SQLException e){
            System.out.println("bruh");
        }
        
        return treeMap;
    }
}
