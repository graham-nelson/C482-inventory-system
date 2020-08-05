package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;
    Product product;
    //TEMPORARY LIST FOR HOLDING ADDED ASSOCIATED PARTS. PARTS ARE ADDED TO THE PRODUCTS ASSOCIATED PARTS LIST
    //WHEN THE PART IS CREATED IN THE onActionSaveButton() METHOD BELOW
    ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> modifyProductPartTable;

    @FXML
    private TableColumn<Part, Integer> partIdTableView;

    @FXML
    private TableColumn<Part, String> partNameTableField;

    @FXML
    private TableColumn<Part, Integer> partInvTableField;

    @FXML
    private TableColumn<Part, Double> partPriceTableField;

    @FXML
    private TableView<Part> productPartTableView;

    @FXML
    private TableColumn<Part, Integer> productPartIdTableField;

    @FXML
    private TableColumn<Part, String> productPartNameTableField;

    @FXML
    private TableColumn<Part, Integer> productPartInvTableField;

    @FXML
    private TableColumn<Part, Double> productPartPriceTableField;

    @FXML
    private TextField modifyProductSearchField;

    @FXML
    private TextField modifyProductId;

    @FXML
    private TextField modifyProductName;

    @FXML
    private TextField modifyProductInv;

    @FXML
    private TextField modifyProductPrice;

    @FXML
    private TextField modifyProductMax;

    @FXML
    private TextField modifyProductMin;

    @FXML
    void onActionAddButton(ActionEvent event) {
        Part selectedPart = modifyProductPartTable.getSelectionModel().getSelectedItem();
        tempAssociatedParts.add(selectedPart);
        productPartTableView.setItems(tempAssociatedParts);
    }

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

    @FXML
    void onActionDeleteButton(ActionEvent event) {
        //IF PART IS SELECTED
        if (productPartTableView.getSelectionModel().getSelectedItem() != null) {
            Part selectedPart = productPartTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "This action will delete Part " + selectedPart.getId() + ". " + "Do you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                getAllProducts().remove(selectedPart);
            }
            tempAssociatedParts.remove(selectedPart);
        }
        //IF NO PART IS SELECTED
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Selection Error");
            alert.setHeaderText("No Part Selected For Removal");
            alert.setContentText("Please Select a Part to Remove");
            alert.showAndWait();
        }
    }

    public void setProduct(Product product) {

        //GLOBAL SO PRODUCT CAN BE REMOVED DURING SAVE BUTTON ON ACTION BELOW
        this.product = product;

        modifyProductId.setText(Integer.toString(product.getId()));
        modifyProductName.setText(product.getName());
        modifyProductInv.setText(Integer.toString(product.getStock()));
        modifyProductPrice.setText(Double.toString(product.getPrice()));
        modifyProductMax.setText(Integer.toString(product.getMax()));
        modifyProductMin.setText(Integer.toString(product.getMin()));
        tempAssociatedParts = product.getAllAssociatedParts();
        productPartTableView.setItems(tempAssociatedParts);
        productPartIdTableField.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartPriceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPartInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    //TAKES MODIFIED DATA FROM TEXT FIELDS AND STORES IN TEMP VARIABLES USED TO CREATE NEW PART
    //OLD PART IS REMOVED TO PREVENT DUPLICATES
    @FXML
    void onActionSaveButton(ActionEvent event) throws IOException {

        //TEMP VARIABLE CREATED FROM MODIFIED INPUT VALUES
        int tempID = Integer.parseInt(modifyProductId.getText());
        String tempName = modifyProductName.getText();
        double tempPrice = Double.parseDouble(modifyProductPrice.getText());
        int tempInv = Integer.parseInt(modifyProductInv.getText());
        int tempMin = Integer.parseInt(modifyProductMin.getText());
        int tempMax = Integer.parseInt(modifyProductMax.getText());
        tempAssociatedParts = product.getAllAssociatedParts();

        //NEW PRODUCT CREATED FROM MODIFIED INPUT VALUES
        Product modifiedProduct = new Product(tempID, tempName, tempPrice, tempInv, tempMin, tempMax);
        for (Part h : tempAssociatedParts)
            modifiedProduct.addAssociatedPart(h);

        try {
            modifiedProduct.inventoryValidCheck();
            Inventory.addProduct(modifiedProduct);

            //PREVIOUSLY MODIFIED PART REMOVED TO PREVENT DUPLICATES
            Inventory.getAllProducts().remove(product);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
        //IF INVENTORY IS MORE THAN MAX OR LESS THAN MIN
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Validating Product");
            alert.setHeaderText("Product not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onActionSearchButton(ActionEvent event) {
        String searchText = modifyProductSearchField.getText();
        for (Part p : getAllParts()){
            if (Objects.equals(p.getName(), searchText) || Objects.equals(Integer.toString(p.getId()), searchText))
                modifyProductPartTable.getSelectionModel().select(p);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //POPULATING TABLE WITH PARTS
        modifyProductPartTable.setItems(getAllParts());
        partIdTableView.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}