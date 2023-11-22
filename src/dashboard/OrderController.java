/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import main.Database;


/**
 *
 * @author binhp
 */
public class OrderController {
    private final Database db;

    public OrderController() throws SQLException {
        this.db = new Database();
    }
    
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
    
    public void setDescTransition(Integer i, Text description){
        String desc = "404 not found";
        
        if (i == 0) desc = "1 day";
        else if (i == 1) desc = "2 days";
        else if (i == 2) desc = "3 days";
        else if (i == 3) desc = "4 days";
        
        description.setText(desc);
    }
    
    public void refeshOrder(ComboBox<String> transType, Text description){
        transType.getItems().clear();
        transType.getItems().addAll(db.getTransportType());
        transType.getSelectionModel().select(0);
        setDescTransition(0, description);
    }
}
