package ams.airlinemanagementsystemos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class airlineController {
    private Stage stage;
    private Scene scene;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Airlines> tvAirlines;

    @FXML
    private TableColumn<Airlines, Integer> colCode;

    @FXML
    private TableColumn<Airlines, String> colName;

    @FXML
    private TableColumn<Airlines, String> colAddress;

    @FXML
    private TableColumn<Airlines, String> colCity;

    @FXML
    private TableColumn<Airlines, String> colState;

    @FXML
    private TableColumn<Airlines, Integer> colZip;

    @FXML
    private TableColumn<Airlines, String> colEmail;

    @FXML
    private TableColumn<Airlines, LocalDate> colLicenseEffectiveDate;

    @FXML
    private TableColumn<Airlines, LocalDate> colLicenseExpiryDate;

    @FXML
    private TableColumn<Airlines, String> colDuration;

    @FXML
    private TableColumn<Airlines, Integer> colTotalFlights;

    @FXML
    private TableColumn<Airlines, Integer> colFlightsDepartToday;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfState;

    @FXML
    private TextField tfZip;

    @FXML
    private TextField tfEmail;

    @FXML
    private DatePicker tfLicenseEffectiveDate;

    @FXML
    private DatePicker tfLicenseExpiryDate;

    @FXML
    private Button btnAirlineInsert;

    @FXML
    private Button btnAirlineUpdate;

    @FXML
    private Button btnAirlineDelete;

    @FXML
        void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToAirports(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        if(airline == null){
            System.out.println("Please select an airline to proceed to airport details.");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("airport.fxml"));
            Parent root = loader.load();

            airportController airC = loader.getController();
            airC.setAirline(airline.getAirline_Code());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    @FXML
    void goToPassengers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("passenger.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("passenger.fxml"));
//        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
    }

    @FXML
    void goToFlights(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        if(airline == null){
            System.out.println("Please select an airline to proceed to flight details.");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flight.fxml"));
            Parent root = loader.load();

            flightController flightc = loader.getController();
            flightc.setAirline(airline.getAirline_Code());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    public Connection getConnection(){
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams", "root", "");
            System.out.println("Connected to db!!!");
            return conn;
        }
        catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
            return null;
        }
    }

    public ObservableList<Airlines> getAirlinesList(){
        ObservableList<Airlines> airlineList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM airline";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Airlines airlines;

            while (rs.next())
            {
                airlines = new Airlines(rs.getInt("Airline_Code"), rs.getString("Airline_Name"), rs.getString("Airline_Address"),rs.getString("Airline_City"),rs.getString("Airline_State"), rs.getInt("Airline_Zip"), rs.getString("Airline_Email"), rs.getDate("License_Issue"),rs.getDate("License_Expiry"));
                airlineList.add(airlines);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return airlineList;
    }

    public void showAirlines(){
        ObservableList<Airlines> list = getAirlinesList();

        colCode.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Airline_Code"));
        colName.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Address"));
        colCity.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_City"));
        colState.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_State"));
        colZip.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Airline_Zip"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Email"));
        colLicenseEffectiveDate.setCellValueFactory(new PropertyValueFactory<Airlines, LocalDate>("License_Issue"));
        colLicenseExpiryDate.setCellValueFactory(new PropertyValueFactory<Airlines, LocalDate>("License_Expiry"));
        colDuration.setCellValueFactory(new PropertyValueFactory<Airlines, String>("duration"));

        tvAirlines.setItems(list);
    }

    public void resetTextField(){
        tfCode.setText("");
        tfName.setText("");
        tfAddress.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfZip.setText("");
        tfEmail.setText("");
        tfLicenseEffectiveDate.setValue(null);
        tfLicenseExpiryDate.setValue(null);
    }

    @FXML
    void handleAirlineCol(MouseEvent event) {
        Airlines airlines =  tvAirlines.getSelectionModel().getSelectedItem();
        tfCode.setText(""+airlines.getAirline_Code());
        tfName.setText(airlines.getAirline_Name());
        tfAddress.setText(airlines.getAirline_Address());
        tfCity.setText(airlines.getAirline_City());
        tfState.setText(airlines.getAirline_State());
        tfZip.setText(""+airlines.getAirline_Zip());
        tfEmail.setText(airlines.getAirline_Email());
        tfLicenseEffectiveDate.setValue(airlines.getLicense_Issue());
        tfLicenseExpiryDate.setValue(airlines.getLicense_Expiry());
    }

    @FXML
    void handleBtnIUDAction(ActionEvent event) {
        if(event.getSource() == btnAirlineInsert){
            insertAirlineRecord();
        }
        else if(event.getSource() == btnAirlineUpdate){
            updateAirlineRecord();
        }
        else if(event.getSource() == btnAirlineDelete){
            deleteAirlineRecord();
        }
    }

    private void deleteAirlineRecord() {
        String query = "DELETE FROM airline WHERE Airline_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    private void updateAirlineRecord() {
        String query = "UPDATE airline SET Airline_Name = '"+ tfName.getText() + "', Airline_Address = '"+ tfAddress.getText() +"', Airline_City = '"+tfCity.getText()+"', Airline_State = '"+tfState.getText()+"', Airline_Zip = "+ Integer.parseInt(tfZip.getText()) +", Airline_Email = '"+ tfEmail.getText() +"', License_Issue = '"+ tfLicenseEffectiveDate.getValue() +"', License_Expiry = '"+ tfLicenseExpiryDate.getValue() +"' WHERE Airline_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    private void insertAirlineRecord() {
        String query = "INSERT INTO airline VALUES ("+ tfCode.getText() + ",'" + tfName.getText() + "','" + tfAddress.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "',"+ tfZip.getText() + ",'" + tfEmail.getText() + "','" + tfLicenseEffectiveDate.getValue() + "','" + tfLicenseExpiryDate.getValue() + "')";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        showAirlines();
    }

}
