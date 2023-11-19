package dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class DashboardController {
    


    @FXML
    private TextField address_rcv_tf;

    @FXML
    private TextField address_snd_tf;

    @FXML
    private ComboBox<?> city_rcv_tf;

    @FXML
    private ComboBox<?> city_snd_tf;

    @FXML
    private CheckBox cod_cb;

    @FXML
    private TextField cod_tf;

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
    private Button manage_btn;

    @FXML
    private AnchorPane manage_pane;

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
    private ComboBox<?> road_snd_tf;

    @FXML
    private Button search_btn;

    @FXML
    private AnchorPane search_pane;

    @FXML
    private ComboBox<?> state_rcv_tf;

    @FXML
    private ComboBox<?> state_snd_tf;

    @FXML
    private ComboBox<?> town_rcv_id;

    @FXML
    private ComboBox<?> town_snd_tf;

    @FXML
    private ComboBox<String> transportType_comboBox;

    @FXML
    private Text transport_text;

    @FXML
    private TextField weight_parcel_tf;
    
    @FXML
    public void switchForm(MouseEvent event){
        AnchorPane[] nav_pane = {home_pane, order_pane, search_pane, manage_pane, report_pane};
        Button[] nav_btn = {home_btn, order_btn, search_btn, manage_btn, report_btn};
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
    
    private OrderController orderController = new OrderController();
        
    @FXML
    private void submitForm(){
        String status = orderController.checkCusForm(
                fname_snd_tf, lname_snd_tf, phone_snd_tf, 
                email_snd_tf, address_snd_tf, state_snd_tf, city_snd_tf);
        System.out.println("sender: " + status);
        
        status = orderController.checkCusForm(fname_rcv_tf, lname_rcv_tf, phone_rcv_tf,
                email_rcv_tf, address_rcv_tf, state_rcv_tf, city_rcv_tf);
        System.out.println("rcv: " + status);        
    }
    
    @FXML
    private void parcelEvent(){
        String status = orderController.checkParcelForm(name_parcel_tf, weight_parcel_tf, qtt_parcel_tf, description_parcel_tf, cod_tf, transportType_comboBox);
        System.out.println(status);
    }
}
