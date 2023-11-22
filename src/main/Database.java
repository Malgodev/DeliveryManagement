/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;

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
    
    public void insertCustomer(String s){
        System.out.println(s);
        IOFile.Write(file_addr, s + "\n");
    }
}
