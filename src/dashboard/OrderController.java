/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author binhp
 */
public class OrderController {
    public String checkCusForm(TextField fname, TextField lname, TextField phone, TextField email, TextField addr, ComboBox<?> state, ComboBox<?> city){
        String[] info = {""};
        if (fname.getText().equals("")) return "no";
        if (lname.getText().equals("")) return "no";
        if (phone.getText().equals("")) return "no";
        if (email.getText().equals("")) return "no";
        if (addr.getText().equals("")) return "no";

        return "yes";
    }        
}
