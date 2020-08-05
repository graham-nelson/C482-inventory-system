package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Optional;

public class ModifyPartController {

    Stage stage;
    Parent scene;
    Part part;


    private Label companyName = new Label("Company Name");


    private Label machineId = new Label("Machine ID");


    private TextField companyNameField = new TextField();


    private TextField machineIdField = new TextField();

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField partTextField;

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

    //THIS METHOD SETS TEXT FIELDS FROM THE PART SELECTED ON THE MAIN SCREEN. FUNCTIONALITY PASSED FROM
    //onActionPartsModifyButton() EVENT HANDLER IN MainScreenController.java
    //CASTS PART TO EITHER INHOUSE OR OUTSOURCED PART
    public void setPart(Part part) {

        //GLOBAL SO PART CAN BE REMOVED DURING SAVE BUTTON ON ACTION BELOW
        this.part = part;

        //CAST part TO INHOUSE IF part IS AN INSTANCE OF INHOUSE CLASS
        if (part instanceof InHouse) {
            InHouse part1 = (InHouse) part;
            inHouseRadioButton.setSelected(true);
            setMachineFields();
            inOutGrid.add(machineId, 0, 0);
            inOutGrid.add(machineIdField, 1, 0);
            machineId.setText("Machine ID");
            machineIdField.setText(Integer.toString(part1.getMachineId()));
            idTextField.setText(Integer.toString(part1.getId()));
            partTextField.setText(part1.getName());
            invTextField.setText(Integer.toString(part1.getStock()));
            priceTextField.setText(Double.toString(part1.getPrice()));
            maxTextField.setText(Integer.toString(part1.getMax()));
            minTextField.setText(Integer.toString(part1.getMin()));
        }

        //CAST part TO OUTSOURCED IF part IS AN INSTANCE OF OUTSOURCED PART
        if (part instanceof Outsourced) {
            Outsourced part1 = (Outsourced) part;
            outsourcedRadioButton.setSelected(true);
            setMachineFields();
            inOutGrid.add(companyName,0, 0);
            inOutGrid.add(companyNameField, 1, 0);
            companyName.setText("Company Name");
            companyNameField.setText(part1.getCompanyName());
            idTextField.setText(Integer.toString(part1.getId()));
            partTextField.setText(part1.getName());
            invTextField.setText(Integer.toString(part1.getStock()));
            priceTextField.setText(Double.toString(part1.getPrice()));
            maxTextField.setText(Integer.toString(part1.getMax()));
            minTextField.setText(Integer.toString(part1.getMin()));
        }
    }

    //RETURNS USER TO MAIN SCREEN WITHOUT MODIFYING OR REMOVING SELECTED PART
    @FXML
    void onActionCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "This action will clear all input data without saving. Do you wish to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
    }

    //TAKES TEXT FIELD DATA AND CREATES NEW PART AND REMOVES OLD PART TO AVOID DUPLICATES
    @FXML
    void onActionSaveButton(ActionEvent event) throws IOException {

        //TEMP VARIABLES USED TO ADD PARTS DEPENDING ON WHICH RADIO BUTTON IS SELECTED
        int tempID = Integer.parseInt(idTextField.getText());
        String tempName = partTextField.getText();
        double tempPrice = Double.parseDouble(priceTextField.getText());
        int tempInv = Integer.parseInt(invTextField.getText());
        int tempMin = Integer.parseInt(minTextField.getText());
        int tempMax = Integer.parseInt(maxTextField.getText());

        //NEW PART CREATED FROM MODIFIED FIELDS
        try {
            if (inHouseRadioButton.isSelected()){
                int tempMachineID = Integer.parseInt(machineIdField.getText());
                InHouse modifiedPart = new InHouse(tempID, tempName, tempPrice, tempInv, tempMin, tempMax, tempMachineID);
                modifiedPart.inventoryValidCheck();
                Inventory.addPart(modifiedPart);
            }
            if (outsourcedRadioButton.isSelected()){
                String tempCompanyName = companyNameField.getText();
                Outsourced modifiedPart = new Outsourced(tempID, tempName, tempPrice, tempInv, tempMin, tempMax, tempCompanyName);
                modifiedPart.inventoryValidCheck();
                Inventory.addPart(modifiedPart);
            }
            //OLD PART IS DELETED TO PREVENT DUPLICATES
            Inventory.getAllParts().remove(part);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
        //IF INVENTORY IS LESS THAN MIN OR MORE THAN MAX
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Validating Part");
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
}

