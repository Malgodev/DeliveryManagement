package dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import main.Connect;
import model.Parcel;
import model.Tracking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReportController {
    Connection connection = Connect.getConnection();
    PreparedStatement statement, state;
    String query;
    ResultSet res, ress;

    ObservableList<Pair<Integer, Integer>> warehouseList = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    void updateTable(TableView<Pair<Integer, Integer>> table, TextField tvMonth){
        warehouseList.clear();
        try{
            query = "select dst_wh#, (coalesce(wh_parcel+cus_parcel, wh_parcel, cus_parcel)) as total_parcel\n" +
                    "from \n" +
                    "(select dst_wh#, count(parcel#) as wh_parcel\n" +
                    "from \n" +
                    "packing natural join (select wh#, pack_date, cargo#, dst_wh# \n" +
                    "                                from transition \n" +
                    "                                where extract(month from pack_date) = ?)\n" +
                    "    group by dst_wh#) \n" +
                    "full outer join (select wh#, count(*) as cus_parcel\n" +
                    "                from sending natural join parcel natural join packing\n" +
                    "                    where extract(month from pack_date) = ?\n" +
                    "group by wh#) on dst_wh# = wh#";
            statement = connection.prepareStatement(query);
            statement.setString(1, tvMonth.getText());
            statement.setString(2, tvMonth.getText());
            statement.executeUpdate();
            res = statement.executeQuery();
            while (res.next()){
                warehouseList.add(new Pair<>(res.getInt(1), res.getInt(2)));
                pieChartData.add(new PieChart.Data(String.valueOf(res.getInt(1)), res.getInt(2)));
            }
            table.setItems(warehouseList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updatePieChart(PieChart wPieChart) {
        wPieChart.getData().clear();
        wPieChart = new PieChart(pieChartData);
        wPieChart.setTitle("Warehouse Report");


    }
}
