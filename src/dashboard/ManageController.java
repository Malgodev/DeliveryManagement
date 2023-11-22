package dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.Connect;
import model.Parcel;

import java.sql.*;
import java.util.List;

public class ManageController {
    Connection connection = Connect.getConnection();
    PreparedStatement statement;
    Statement state = connection.createStatement();
    String query;
    ResultSet res;

    ObservableList<Parcel> parcelList = FXCollections.observableArrayList();

    public ManageController() throws SQLException {
    }

    public void initTable(TableView<Parcel> table){
        try {
            res = state.executeQuery("select * from parcel");
            while (res.next()) {
                parcelList.add(new Parcel(res.getInt("parcel#"),res.getDouble("weight"), res.getInt("transport#"), res.getString("title"), res.getString("note"), res.getInt("COD"), res.getInt("COD_status"), res.getInt("status")));
//                System.out.println(res.getInt("parcel#"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        table.setItems(parcelList);
    }
}
