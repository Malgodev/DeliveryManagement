package dashboard;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import java.security.interfaces.DSAKey;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.util.Callback;
import javafx.util.Pair;
import main.Database;
import main.Validate;
import model.Parcel;
import model.Tracking;

public class DashboardController implements Initializable {

    @FXML
    private TextField address_rcv_tf;

    @FXML
    private TextField address_snd_tf;

    @FXML
    private CheckBox cod_cb;

    @FXML
    private TextField cod_tf;

    @FXML
    private Button createParcel_btn;

    @FXML
    private TextArea description_parcel_tf;

    @FXML
    private TextField email_rcv_tf;

    @FXML
    private TextField email_snd_tf;

    @FXML
    private TextField fname_rcv_tf;

    @FXML
    private TextField fname_snd_tf;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_pane;

    @FXML
    private AnchorPane invoice_text;

    @FXML
    private TextField lname_rcv_tf;

    @FXML
    private TextField lname_snd_tf;

    @FXML
    private Text monthly_avrWeight;

    @FXML
    private LineChart<String, Number> monthly_home_graph;

    @FXML
    private Text monthly_order;

    @FXML
    private Text monthly_pay;

    @FXML
    private TextField name_parcel_tf;

    @FXML
    private Button order_btn;

    @FXML
    private AnchorPane order_pane;

    @FXML
    private TextField phone_rcv_tf;

    @FXML
    private TextField phone_snd_tf;

    @FXML
    private TextField qtt_parcel_tf;

    @FXML
    private Button report_btn;

    @FXML
    private AnchorPane report_pane;

    @FXML
    private Button search_btn;

    @FXML
    private AnchorPane search_pane;

    @FXML
    private ComboBox<String> transportType_comboBox;

    @FXML
    private Text transport_text;

    @FXML
    private TextField weight_parcel_tf;
    
    @FXML
    private ComboBox<String> home_month_cb;    
    
    @FXML
    private TextField zip_rcv_tf;

    @FXML
    private TextField zip_snd_tf;

////////////////////////////////////////////////////////////////////////////////    
  
    private OrderController orderController = new OrderController();

    private Database db = new Database();
    
    private Integer home_month;

    public DashboardController() throws SQLException {
    }


    private HomeController homeController = new HomeController();
    


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addHomeComboBox();
        refeshAll();
        initParcelTable();
        initForm();
        initReportTable();
    }

    private void initForm() {
        AnchorPane[] nav_pane = {home_pane, order_pane, search_pane, report_pane};
        Button[] nav_btn = {home_btn, order_btn, search_btn, report_btn};
        for (int i = 1; i < nav_pane.length; i++){
            nav_pane[i].setVisible(false);
            nav_btn[i].setStyle("-fx-background-color: transparent;");
        }
        nav_pane[0].setVisible(true);
    }

    @FXML
    private void refeshAll(){
        homeController.refeshHome(monthly_order, monthly_pay, monthly_avrWeight, monthly_home_graph, home_month);
        orderController.refeshOrder(transportType_comboBox, transport_text);
    }    
    
    @FXML
    public void changeTransDescription(){
        orderController.setDescTransition(transportType_comboBox.getSelectionModel().getSelectedIndex(), transport_text);
    }
    
    @FXML
    public void switchForm(MouseEvent event){
        AnchorPane[] nav_pane = {home_pane, order_pane, search_pane, report_pane};
        Button[] nav_btn = {home_btn, order_btn, search_btn, report_btn};
        for (int i = 0; i < nav_pane.length; i++){
            nav_pane[i].setVisible(false);
            nav_btn[i].setStyle("-fx-background-color: transparent;");
        }
        
        Object btn_src = event.getSource();
        for (int i = 0; i < nav_pane.length; i++){
            if (btn_src == nav_btn[i]){
                nav_pane[i].setVisible(true);
                nav_btn[i].setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
            }
        }
    }
    
        
    @FXML
    private void submitForm(){
        System.out.println("cust event");
    }
    
    @FXML
    private void parcelEvent(){
        System.out.println("parcel event");
//        ArrayList<String> status = orderController.checkParcelForm(name_parcel_tf, weight_parcel_tf, qtt_parcel_tf, 
//                description_parcel_tf, cod_tf, (ComboBox<String>) transportType_comboBox);
//        
//        return status;
    }
    Validate validate = new Validate();
    Effect invalidEffect = new DropShadow(BlurType.GAUSSIAN, Color.RED, 10, 0.0, 0, 0);

    @FXML
    private void validatePhone(){
        if(!validate.isMobileNumberValid(phone_snd_tf.getText())){
            phone_snd_tf.setEffect(invalidEffect);
        }else phone_snd_tf.setEffect(null);
    }

    @FXML
    private void validateSenderZip(){
        if(!validate.isZipValid(zip_snd_tf.getText())){
            zip_snd_tf.setEffect(invalidEffect);
        }else zip_snd_tf.setEffect(null);
    }

    @FXML
    private void validateReceiverZip(){
        if(!validate.isZipValid(zip_rcv_tf.getText())){
            zip_rcv_tf.setEffect(invalidEffect);
        }else zip_rcv_tf.setEffect(null);
    }

    @FXML
    private void validateReiPhone(){
        if(!validate.isMobileNumberValid(phone_rcv_tf.getText())){
            phone_rcv_tf.setEffect(invalidEffect);
        }else phone_rcv_tf.setEffect(null);
    }

    @FXML
    private void createOrder(){ // missing parcel
        try{
            if(!validate.isZipValid(zip_snd_tf.getText()) || !validate.isZipValid(zip_rcv_tf.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid zip-code");
                alert.setContentText("Please enter a valid zip-code include 5 digits");
                alert.showAndWait();
                return;
            }

            if(!validate.isMobileNumberValid(phone_snd_tf.getText()) || !validate.isMobileNumberValid(phone_rcv_tf.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid phone number");
                alert.setContentText("Please enter a valid phone number include 10 digits");
                alert.showAndWait();
                return;
            }

            ArrayList<String> status_snd = orderController.getCustInfo(fname_snd_tf, lname_snd_tf, phone_snd_tf,
                    email_snd_tf, address_snd_tf, zip_snd_tf);   
            
            ArrayList<String> status_rcv = orderController.getCustInfo(fname_rcv_tf, lname_rcv_tf, phone_rcv_tf,
                    email_rcv_tf, address_rcv_tf, zip_rcv_tf);
            
            ArrayList<String> status_parcel = orderController.getParcelForm(name_parcel_tf, weight_parcel_tf, qtt_parcel_tf, 
                description_parcel_tf, cod_cb, cod_tf, transportType_comboBox);
            
            if (!status_snd.isEmpty() && !status_rcv.isEmpty() && !status_parcel.isEmpty()){
                db.createParcel(status_snd, status_rcv, status_parcel);
            }
            
        }catch (NullPointerException e){
            System.out.println("Null pointer exception");
        }

    }
    
    @FXML
    private void getHomeMonth(){
        home_month = home_month_cb.getSelectionModel().getSelectedIndex() + 1;
        refeshAll();
    }

    private void addHomeComboBox(){
        ObservableList<String> month_name = FXCollections.observableArrayList(
                "January", 
                "February", 
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        );
        
        home_month_cb.setItems(month_name);
        home_month_cb.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        home_month = LocalDate.now().getMonthValue();
    }

    
//--------------- ManageController ----------------
//--------------- Cook, Leave me alone ----------------
    private ManageController manage = new ManageController();

    @FXML
    private TableColumn<Parcel, Integer> p_cod;
    @FXML
    private TableColumn<Parcel, String> p_sender;
    @FXML
    private TableColumn<Parcel, String> p_recipient;
    @FXML
    private TableColumn<Parcel, String> p_codStatus;
    @FXML
    private TableColumn<Parcel, Integer> p_id;
    @FXML
    private TableColumn<Parcel, String> p_status;
    @FXML
    private TableView<Parcel> tbParcel;
    @FXML
    private TextArea tvDes;
    @FXML
    private TextField tvTitle;
    @FXML
    private TextField tvWeight;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private ComboBox<String> cbCodStatus;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField tvMin;
    @FXML
    private TextField tvMax;
    @FXML
    private TableView<Tracking> tbTracking;
    @FXML
    private TableColumn<Tracking, String> p_date;
    @FXML
    private TableColumn<Tracking, String> p_add;
    @FXML
    void showParcelInfo(){
        manage.displayInformation(tbParcel, tvTitle, tvWeight, tvDes);
        manage.trackParcel(tbTracking, tbParcel);
    }
    @FXML
    void searchTable() throws SQLException {
        manage.searchParcel(cbSearch, searchBar, tbParcel);
        showParcelInfo();
    }
    @FXML
    void updateParcel() throws SQLException {
        manage.updateParcel(tvTitle, tvWeight, tvDes, tbParcel);
        Parcel parcel = tbParcel.getSelectionModel().getSelectedItem();
        parcel.setTitle(tvTitle.getText());
        parcel.setNote(tvDes.getText());
        parcel.setWeight(Double.parseDouble(tvWeight.getText()));
        tbParcel.refresh();
        System.out.println("Updated");
    }
    @FXML
    void filterParcel() throws SQLException {
        manage.filterParcel(cbStatus, cbCodStatus, tvMin, tvMax, tbParcel);
        showParcelInfo();
    }
    void initParcelTable(){
        p_id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Parcel, Integer> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getId());
                else
                    return new ReadOnlyObjectWrapper<>(0);
            }
        });

        p_sender.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parcel, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getSender());
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });

        p_recipient.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parcel, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getReceiver());
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });

        p_cod.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Parcel, Integer> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getCOD());
                else
                    return new ReadOnlyObjectWrapper<>(0);
            }
        });

        p_codStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parcel, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getCOD_status());
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });
        p_status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parcel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parcel, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Parcel) p.getValue()).getStatus());
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });

        p_date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tracking, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tracking, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Tracking) p.getValue()).getDate()).asString();
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });

        p_add.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tracking, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tracking, String> p) {
                if (p.getValue() != null)
                    return new ReadOnlyObjectWrapper<>(((Tracking) p.getValue()).getAddress());
                else
                    return new ReadOnlyObjectWrapper<>("");
            }
        });

        manage.initTable(tbParcel);
        manage.displayInformation(tbParcel, tvTitle, tvWeight, tvDes);
        manage.initComboBox(cbSearch, cbStatus, cbCodStatus);
    }

    //--------------- ReportController ----------------
    //--------------- Cook, Leave me alone ----------------
    private ReportController report = new ReportController();
    @FXML
    private TableView<Pair<Integer, Integer>> tbWH;
    @FXML
    private TableColumn<Pair<Integer, Integer>, Integer> w_id;
    @FXML
    private TableColumn<Pair<Integer, Integer>, Integer> w_total;
    @FXML
    private TextField tvMonth;
    @FXML
    void updateWH() throws SQLException {
        report.updateTable(tbWH, tvMonth);
    }
    @FXML
    private PieChart w_pieChart;
    void initReportTable(){
           w_id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<Integer, Integer>, Integer>, ObservableValue<Integer>>() {
               public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Pair<Integer, Integer>, Integer> p) {
                   if (p.getValue() != null)
                       return new ReadOnlyObjectWrapper<>(((Pair<Integer, Integer>) p.getValue()).getKey());
                   else
                       return new ReadOnlyObjectWrapper<>(0);
               }
           });

              w_total.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<Integer, Integer>, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Pair<Integer, Integer>, Integer> p) {
                     if (p.getValue() != null)
                          return new ReadOnlyObjectWrapper<>(((Pair<Integer, Integer>) p.getValue()).getValue());
                     else
                          return new ReadOnlyObjectWrapper<>(0);
                }
              });

              tvMonth.setText(String.valueOf(LocalDate.now().getMonthValue()));

              report.updateTable(tbWH, tvMonth);
              report.updatePieChart(w_pieChart);
    }
}
