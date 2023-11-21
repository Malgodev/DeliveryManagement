/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import main.Database;

/**
 *
 * @author binhp
 */
public class HomeController {    
    private final Database db = new Database();
    
    public void refeshHome(Text order, Text payment, Text weight, LineChart<String, Number> chart){
        order.setText(db.getMonthlyOrder().toString());
        payment.setText(db.getMonthlyPayment().toString());
        weight.setText(db.getMonthlyAvgWeight().toString());
        
        chart.getData().clear();
        chart.getData().add(db.getMonthlyParcelPerDay());
    }
}
