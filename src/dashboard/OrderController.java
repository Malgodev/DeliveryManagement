/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
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
    
    public ArrayList<String> getParcelForm(TextField name, TextField weight, TextField qtt, TextArea description, CheckBox isCOD ,TextField cod, ComboBox<String> trans_type){
        ArrayList<String> info = new ArrayList<>();
        
        if (name.getText().equals("")) return null;
        else info.add(name.getText());
        
        try{
            if (weight.getText().equals("") || qtt.getText().equals("")){
                return null;
            }else{
                Integer total_weight = Integer.parseInt(weight.getText()) * Integer.parseInt(qtt.getText()); 
                info.add(total_weight.toString());
            }
        }catch (NumberFormatException ex){
            System.out.println("Must be a number!");
            return null;
        }
        info.add(description.getText());
        
        
        if(isCOD.isSelected()){
            if (cod.getText().equals("")) return null;
            else info.add(cod.getText());
        }else{
            info.add("");
        }
        
        info.add(String.valueOf(trans_type.getSelectionModel().getSelectedIndex()));
        
        return info;
    }
    
    public ArrayList<String> getCustInfo(TextField fname, TextField lname, TextField phone, TextField email, TextField addr, TextField zip){
        ArrayList<String> info = new ArrayList<>();
        if (fname.getText().equals("")) return null;
        else info.add((fname.getText() + " " + lname.getText()).trim());
        
        if (phone.getText().equals("")) return null;
        else info.add(phone.getText());
        
        info.add(email.getText());
        
        if (addr.getText().equals("")) return null;
        else info.add(addr.getText());
 
        if (zip.getText().equals("")) return null;
        else info.add(zip.getText());        
                
        return info;
    }
    
    public void setDescTransition(Integer i, Text description){
        String desc = "404 not found";
        
        if (i == 0) desc = "Consistent progression of cargo, maintaining regular schedules and procedures, ensuring steady movement without deviations or sudden changes.";
        else if (i == 1) desc = "Optimizing logistics for swift cargo movement, prioritizing speed to meet deadlines efficiently, minimizing transit durations for timely deliveries.";
        else if (i == 2) desc = "Personalized and accelerated cargo services, catering to urgent shipments with specialized handling, dedicated support, and premium delivery options.";
        else if (i == 3) desc = "Immediate responses to unexpected disruptions, swiftly adapting routes, managing sudden inventory changes, ensuring continuous cargo flow despite unforeseen challenges.";
        
        description.setText(desc);
    }
    
    public void refeshOrder(ComboBox<String> transType, Text description){
        transType.getItems().clear();
        transType.getItems().addAll(db.getTransportType());
        transType.getSelectionModel().select(0);
        setDescTransition(0, description);
    }
}
