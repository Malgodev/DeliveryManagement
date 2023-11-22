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
            "Waiting for transiting",
            "Transiting",
            "Delivering",
            "Waiting for re-delivering",
            "Delivered",
            "Processing",
            "Waiting for returning",
            "Returning",
            "Returned",
            "404 not found");

    ObservableList<String> codStatuss = FXCollections.observableArrayList("COD Status", "No-COD", "Haven't paid", "Paid");
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
        status.setValue("Status");
        codStatus.setItems(codStatuss);
        codStatus.setValue("COD Status");
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

    String convertStatus(String status){
        return switch (status){
            case "Waiting for transiting" -> "WHERE status = 0";
            case "Transiting" -> "WHERE status = 1";
            case "Delivering" -> "WHERE status = 2";
            case "Waiting for re-delivering" -> "WHERE status = 3";
            case "Delivered" -> "WHERE status = 4";
            case "Processing" -> "WHERE status = 5";
            case "Waiting for returning" -> "WHERE status = 6";
            case "Returning" -> "WHERE status = 7";
            case "Returned" -> "WHERE status = 8";
            default -> "WHERE status = status";
        };

    }

    String convertCodStatus(String status){
        return switch (status){
            case "No-COD" -> "and COD_status is null";
            case "Haven't paid" -> "and COD_status = 0";
            case "Paid" -> "and COD_status = 1";
            default -> "and COD_status = COD_status";
        };
    }

    String convertWeight(Double min, Double max){
        return "and weight between " + min + " and " + max;
    }

    void filterParcel(ComboBox<String> status, ComboBox<String> codStatus, TextField tvMin, TextField tvMax, TableView<Parcel> table) throws SQLException {
        String statusType = status.getSelectionModel().getSelectedItem();
        String codStatusType = codStatus.getSelectionModel().getSelectedItem();
        Double min = Double.parseDouble(tvMin.getText());
        Double max = Double.parseDouble(tvMax.getText());
        try {
            query = "SELECT * FROM parcel " + convertStatus(statusType) + " " + convertCodStatus(codStatusType) + " " + convertWeight(min, max);
            System.out.println(query);
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            updateTable(table, res);

        } catch (SQLException ignored) {
        }
    }
}

