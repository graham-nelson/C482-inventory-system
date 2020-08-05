package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    private Label companyName = new Label("Company Name");


    private Label machineId = new Label("Machine ID");


    private TextField companyNameField = new TextField();


    private TextField machineIdField = new TextField();

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    //THIS TEXTFIELD IS UNUSED DUE TO AUTO-POPULATING THE ID'S OF NEW PARTS
    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField invTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private GridPane inOutGrid;

    @FXML
    void onActionAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "This action will clear all input data without saving. Do you wish to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        //RETURNS USER TO MAIN SCREEN
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
    }

    //METHOD USED TO TAKE USER INPUT IN TEXTFIELD AND CREATE A NEW PART FROM THOSE FIELD TO BE
    //DISPLAYED IN TABLEVIEW ON MAIN SCREEN. PARTS ADDED TO allParts OBSERVABLE LIST
    @FXML
    void onActionAddPartSave(ActionEvent event) throws IOException {

        //TAKES TEXTFIELD INPUT VALUES AND STORES THEM IN TEMP VARIABLE TO BE USED IN addPart() METHOD BELOW
        int tempID = (Inventory.getAllParts().size()) + 1;
        String tempName = nameTextField.getText();
        double tempPrice = Double.parseDouble(priceTextField.getText());
        int tempInv = Integer.parseInt(invTextField.getText());
        int tempMin = Integer.parseInt(minTextField.getText());
        int tempMax = Integer.parseInt(maxTextField.getText());

        try {
            //TEMP VARIABLES USED TO ADD PARTS DEPENDING ON WHICH RADIO BUTTON IS SELECTED
            if (inHouseRadioButton.isSelected()){
                int tempMachineID = Integer.parseInt(machineIdField.getText());
                InHouse part = new InHouse(tempID, tempName, tempPrice, tempInv, tempMin, tempMax, tempMachineID);
                part.inventoryValidCheck();
                Inventory.addPart(part);
            }
            if (outsourcedRadioButton.isSelected()){
                String tempCompanyName = companyNameField.getText();
                Outsourced part = new Outsourced(tempID, tempName, tempPrice, tempInv, tempMin, tempMax, tempCompanyName);
                part.inventoryValidCheck();
                Inventory.addPart(part);
            }

            //RETURNS USER TO MAIN SCREEN
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
        //IF INVENTORY IS GREATER THAN MAX OR LESS THAN MIN
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Validating New Part");
            alert.setHeaderText("Part not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setCompanyFields(){
        companyNameField.setPromptText("Company Name");
        companyNameField.setStyle("-fx-background-radius: 50");
        companyName.setFont(new Font(14));
    }

    public void setMachineFields(){
        machineIdField.setPromptText("Machine ID");
        machineIdField.setStyle("-fx-background-radius: 50");
        machineId.setFont(new Font(14));
    }

    public void outsourcedButtonSelected(){
        if (outsourcedRadioButton.isSelected()){
            inOutGrid.getChildren().removeAll(companyName, machineId, machineIdField, companyNameField);

            setCompanyFields();

            inOutGrid.add(companyName,0, 0);
            inOutGrid.add(companyNameField, 1, 0);
        }
    }

    public void inHouseButtonSelected(){
        if (inHouseRadioButton.isSelected()){
            inOutGrid.getChildren().removeAll(companyName, machineId, machineIdField, companyNameField);

            setMachineFields();

            inOutGrid.add(machineId, 0, 0);
            inOutGrid.add(machineIdField, 1, 0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //SETS THE INHOUSE RADIO BUTTON TO BE SELECTED AND TO HAVE THE MACHINE ID FIELD SHOWN AS DEFAULT ON SCENE LOAD
        inHouseRadioButton.setSelected(true);
        if (inHouseRadioButton.isSelected()) {
            inOutGrid.getChildren().removeAll(companyName, machineId, machineIdField, companyNameField);

            setMachineFields();

            inOutGrid.add(machineId, 0, 0);
            inOutGrid.add(machineIdField, 1, 0);
        }
    }
}


