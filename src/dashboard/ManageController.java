package dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    ObservableList<String> searchs = FXCollections.observableArrayList("All", "ID", "Title");
    ObservableList<String> parcelStatus = FXCollections.observableArrayList("Status", "Delivering", "Delivered");

    ObservableList<String> codStatuss = FXCollections.observableArrayList("COD Status", "Yes", "No");
    public ManageController() throws SQLException {
    }

    public void initTable(TableView<Parcel> table){
        try {
            res = state.executeQuery("select * from parcel");
            while (res.next()) {
                parcelList.add(new Parcel(res.getInt("parcel#"),
                        res.getDouble("weight"),
                        res.getInt("transport#"),
                        res.getString("title"),
                        res.getString("note"),
                        res.getInt("COD"),
                        res.getInt("COD_status"),
                        res.getInt("status"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        table.setItems(parcelList);
        table.getSelectionModel().selectFirst();
    }

    void updateTable(TableView<Parcel> table, ResultSet res) {
        try {
            parcelList.clear();
            while (res.next()) {
                parcelList.add(new Parcel(res.getInt("parcel#"),
                        res.getDouble("weight"),
                        res.getInt("transport#"),
                        res.getString("title"),
                        res.getString("note"),
                        res.getInt("COD"),
                        res.getInt("COD_status"),
                        res.getInt("status"))
                );
                table.setItems(parcelList);
            }

        } catch (SQLException ignored) {
        }

    }

    void displayInformation(TableView<Parcel> table, TextField title, TextField weight, TextArea note){
        Parcel parcel = table.getSelectionModel().getSelectedItem();
        String pTitle = parcel.getTitle();
        String pWeight = String.valueOf(parcel.getWeight());
        String pNote = parcel.getNote();

        if(pTitle == null){
            title.setText("NO-NAME");
        }else title.setText(pTitle);

        if(pWeight == null) {
            weight.setText("0");
        }else weight.setText(pWeight);

        if(pNote == null) {
            note.setText("NO-NOTE");
        }else note.setText(pNote);
    }

    void initComboBox(ComboBox<String> search,ComboBox<String> status, ComboBox<String> codStatus){
        search.setItems(searchs);
        search.setValue("ID");
        status.setItems(parcelStatus);
        codStatus.setItems(codStatuss);
    }



    void searchParcel(ComboBox<String> search, TextField searchBar, TableView<Parcel> table) throws SQLException {
        String searchType = search.getSelectionModel().getSelectedItem();
        if(searchType.equals("ID")){
            try {
                query = "SELECT * FROM parcel WHERE PARCEL# = "  + searchBar.getText();
                statement = connection.prepareStatement(query);
                res = statement.executeQuery();
                updateTable(table, res);

            } catch (SQLException ignored) {
            }

        }else if(searchType.equals("Title")){
            System.out.println("Title");
            try {

                query = "SELECT * FROM parcel WHERE title LIKE '%" + searchBar.getText() + "%'";
                statement = connection.prepareStatement(query);
                res = statement.executeQuery();
                updateTable(table, res);

            } catch (SQLException ignored) {
            }
        }
    }
}

