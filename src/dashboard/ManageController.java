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
import java.util.Objects;

public class ManageController {
    Connection connection = Connect.getConnection();
    PreparedStatement statement;
    Statement state = connection.createStatement();
    String query;
    ResultSet res;

    ObservableList<Parcel> parcelList = FXCollections.observableArrayList();
    ObservableList<String> searchs = FXCollections.observableArrayList( "ID", "Title");
    ObservableList<String> parcelStatus = FXCollections.observableArrayList(
            "Status",
            "Wait for transiting",
            "Transiting",
            "Delivering",
            "Wait for re-delivering",
            "Delivered",
            "Processing",
            "Wait for returning",
            "Returning",
            "Returned",
            "404 not found");

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
                table.getSelectionModel().selectFirst();
//                table.refresh();
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
//                statement = connection.prepareStatement(query);
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

    void updateParcel(TextField title, TextField weight, TextArea note, TableView<Parcel> table) throws SQLException {
        Parcel parcel = table.getSelectionModel().getSelectedItem();
        int id = parcel.getId();
        String pTitle = title.getText();
        String pWeight = weight.getText();
        String pNote = note.getText();
        try {
            query = "UPDATE parcel SET title = ?, weight = ?, note = ? WHERE PARCEL# = " + id;
            statement = connection.prepareStatement(query);
            statement.setString(1, pTitle);
            statement.setString(2, pWeight);
            statement.setString(3, pNote);
            statement.executeUpdate();


        } catch (SQLException ignored) {
        }
    }

    int convertStatus(String status){
        return switch (status){
            case "Waiting for transiting" -> 0;
            case "Transiting" -> 1;
            case "Delivering" -> 2;
            case "Waiting for re-delivering" -> 3;
            case "Delivered" -> 4;
            case "Processing" -> 5;
            case "Waiting for returning" -> 6;
            case "Returning" -> 7;
            case "Returned" -> 8;
            default -> -1;
        };
    }

    void filterParcel(ComboBox<String> status, ComboBox<String> codStatus, TableView<Parcel> table) throws SQLException {
        Integer codStatusType = Objects.equals(codStatus.getSelectionModel().getSelectedItem(), "Yes") ? 1 : 0;
        String statusType = status.getSelectionModel().getSelectedItem();
        try {
            query = "SELECT * FROM parcel WHERE status = '" + convertStatus(statusType) + "' AND COD_status = '" + codStatusType + "'";
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            updateTable(table, res);

        } catch (SQLException ignored) {
        }
    }
}

