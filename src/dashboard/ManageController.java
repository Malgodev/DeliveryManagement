package dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Connect;
import model.Parcel;
import model.Tracking;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

public class ManageController {
    Connection connection = Connect.getConnection();
    PreparedStatement statement;
    Statement state = connection.createStatement();
    String query;
    ResultSet res;

    ObservableList<Parcel> parcelList = FXCollections.observableArrayList();
    ObservableList<Tracking> trackList = FXCollections.observableArrayList();
    ObservableList<String> searchs = FXCollections.observableArrayList( "ID", "Title", "Sender", "Recipient");
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
            res = state.executeQuery("select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer#");
            while (res.next()) {
                parcelList.add(new Parcel(res.getInt("parcel#"),
                        res.getDouble("weight"),
                        res.getInt("transport#"),
                        res.getString("title"),
                        res.getString("note"),
                        res.getInt("COD"),
                        res.getInt("COD_status"),
                        res.getInt("status"),
                        res.getString("sender"),
                        res.getString("recipient"))
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
                        res.getInt("status"),
                        res.getString("sender"),
                        res.getString("recipient"))
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
                query = "select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer# and PARCEL# = "  + searchBar.getText();
                statement = connection.prepareStatement(query);
                res = statement.executeQuery();
                updateTable(table, res);

            } catch (SQLException ignored) {
            }

        }else if(searchType.equals("Title")){
            System.out.println("Title");
            try {

                query = "select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer# and title LIKE '%" + searchBar.getText() + "%'";
                statement = connection.prepareStatement(query);
                res = statement.executeQuery();
                updateTable(table, res);

            } catch (SQLException ignored) {
            }
        }else if(searchType.equals("Sender")) {
            try {
                query = "select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer# and A.full_name LIKE '%" + searchBar.getText() + "%'";
                statement = connection.prepareStatement(query);
                res = statement.executeQuery();
                updateTable(table, res);

            } catch (SQLException ignored) {
            }
        }else if (searchType.equals("Recipient")){
            try {
                query = "select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer# and B.full_name LIKE '%" + searchBar.getText() + "%'";
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
            case "Waiting for transiting" -> "and status = 0";
            case "Transiting" -> "and status = 1";
            case "Delivering" -> "and status = 2";
            case "Waiting for re-delivering" -> "and status = 3";
            case "Delivered" -> "and status = 4";
            case "Processing" -> "and status = 5";
            case "Waiting for returning" -> "and status = 6";
            case "Returning" -> "and status = 7";
            case "Returned" -> "and status = 8";
            default -> "and status = status";
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
            query = "select parcel#, weight, transport#, title, note, COD, COD_status, status, send_date, A.full_name as sender, B.full_name as recipient from parcel natural join sending, customer A, customer B where sender# = A.customer# and recipient# = B.customer# " + convertStatus(statusType) + " " + convertCodStatus(codStatusType) + " " + convertWeight(min, max);
            System.out.println(query);
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();
            updateTable(table, res);

        } catch (SQLException ignored) {
        }
    }

    void trackParcel(TableView<Tracking> tableT, TableView<Parcel> tableP){
        trackList.clear();
        Parcel parcel = tableP.getSelectionModel().getSelectedItem();
        try {
            query = "select send_date, address from sending natural join parcel natural join packing, warehouse where packing.wh# = warehouse.wh# and parcel# = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, parcel.getId());
            statement.executeUpdate();
            res = statement.executeQuery();
            while (res.next()) {
                trackList.add(new Tracking(res.getDate("send_date"),
                        res.getString("address"))
                );
            }
            //2
            query = "select pack_date, wh#, cargo# from packing where parcel# = " + 0;
            statement = connection.prepareStatement(query);
            res = statement.executeQuery();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
            while (res.next()) {
                query = "select transit_date, address from transition, warehouse where transition.wh# = " + res.getInt("wh#") +
                        " and transition.pack_date = to_date('" + formatter.format(res.getDate("pack_date")) +"','dd-mm-yy') and transition.cargo# = " + res.getInt("cargo#") +
                        " and transition.dst_wh# = warehouse.wh#";

                PreparedStatement prStatement = connection.prepareStatement(query);
                ResultSet result = prStatement.executeQuery();
                while (result.next()) {
                   trackList.add(new Tracking(result.getDate("transit_date"),
                           result.getString("address"))
                   );
                }
            }
//            //3
            query = "select delivery_date, address\n" +
                    "from delivery join parcel using (parcel#) natural join sending, customer\n" +
                    "where recipient# = customer# and parcel# = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, parcel.getId());
            statement.executeUpdate();
            res = statement.executeQuery();
            while (res.next()) {
                trackList.add(new Tracking(res.getDate("delivery_date"),
                        res.getString("address"))
                );
            }

            tableT.setItems(trackList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

