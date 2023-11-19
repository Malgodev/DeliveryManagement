/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author binhp
 */
public class OrderController {
    public String checkCusForm(TextField fname, TextField lname, TextField phone, TextField email, TextField addr, ComboBox<?> state, ComboBox<?> city){
        String info = "";
        if (fname.getText().equals("")) return "no";
        else info += fname.getText() + " ";
        if (lname.getText().equals("")) return "no";
        else info += lname.getText() + ", ";
        if (phone.getText().equals("")) return "no";
        else info += phone.getText() + ", ";
        if (email.getText().equals("")) return "no";
        else info += email.getText() + ", ";
        if (addr.getText().equals("")) return "no";
        else info += addr.getText();
        
        return info;
    }        
    
    public String checkParcelForm(TextField name, TextField weight, TextField qtt, TextArea description, TextField cod, ComboBox<String> trans_type){
        String info = "";
        if (name.getText().equals("")) return "no";
        else info += name.getText() + ", ";
        try{
            if (weight.getText().equals("") || qtt.getText().equals("")){
                return "no";
            }else{
                Integer total_weight = Integer.parseInt(weight.getText()) * Integer.parseInt(qtt.getText()); 
                info += total_weight + ", ";
            }
        }catch (NumberFormatException ex){
            System.out.println("Must be a number!");
            return "no";
        }
        info += description.getText() + ", ";
        
        if (cod.getText().equals("")) return "no";
        else info += cod.getText() + ", ";
        
        return info;
    }
}
