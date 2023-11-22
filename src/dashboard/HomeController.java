/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;

import java.sql.SQLException;
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
    private final Database db;

    public HomeController() throws SQLException {
        this.db = new Database();
    }
    
    public void refeshHome(Text order, Text payment, Text weight, LineChart<String, Number> chart, int month){
        
        order.setText(db.getMonthlyOrder(month).toString());
        payment.setText(db.getMonthlyPayment(month));
        weight.setText(db.getMonthlyAvgWeight(month).toString());
        
        chart.getData().clear();
        chart.getData().add(db.getMonthlyParcelPerDay(month));
    }
}
