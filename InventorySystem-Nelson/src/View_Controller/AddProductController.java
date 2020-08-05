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

public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;
    //TEMPORARY LIST FOR HOLDING ADDED ASSOCIATED PARTS. PARTS ARE ADDED TO THE PRODUCTS ASSOCIATED PARTS LIST
    //WHEN THE PART IS CREATED IN THE onActionSaveButton() METHOD BELOW
    ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();


    @FXML
    private TableView<Part> addPartTableView;

    @FXML
    private TableColumn<Part, Integer> addIdTableField;

    @FXML
    private TableColumn<Part, String> addNameTableField;

    @FXML
    private TableColumn<Part, Integer> addInvTableField;

    @FXML
    private TableColumn<Part, Double> addPriceTableField;

    @FXML
    private TableView<Part> productPartsTableView;

    @FXML
    private TableColumn<Part, Integer> productPartIdTableField;

    @FXML
    private TableColumn<Part, String> productPartNameTableField;

    @FXML
    private TableColumn<Part, Integer> productPartInvTableField;

    @FXML
    private TableColumn<Part, Double> productPartPriceTableField;

    @FXML
    private TextField addProductSearchField;

    @FXML
    private TextField addProductId;

    @FXML
    private TextField addProductName;

    @FXML
    private TextField addProductInv;

    @FXML
    private TextField addProductPrice;

    @FXML
    private TextField addProductMax;

    @FXML
    private TextField addProductMin;

    @FXML
    void onActionAddButton(ActionEvent event) throws IOException {
        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();
        tempAssociatedParts.add(selectedPart);
        productPartsTableView.setItems(tempAssociatedParts);
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
    void onActionDeleteButton(ActionEvent event)  {
        //IF PART IS SELECTED
        if (productPartsTableView.getSelectionModel().getSelectedItem() != null) {
            Part selectedPart = productPartsTableView.getSelectionModel().getSelectedItem();
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

    //TAKES DATA FROM INPUT FIELD AND CREATES TEMP VARIABLES THAT ARE USED IN PRODUCT CONSTRUCTOR TO CREATE NEW PRODUCT
    @FXML
    void onActionSaveButton(ActionEvent event) throws IOException {
        int tempID = (Inventory.getAllProducts().size()) + 1;
        String tempName = addProductName.getText();
        double tempPrice = Double.parseDouble(addProductPrice.getText());
        int tempInv = Integer.parseInt(addProductInv.getText());
        int tempMin = Integer.parseInt(addProductMin.getText());
        int tempMax = Integer.parseInt(addProductMax.getText());


        Product product = new Product(tempID, tempName, tempPrice, tempInv, tempMin, tempMax);
        //ADDS SELECTED PARTS TO ASSOCIATED PARTS LIST
        for (Part h : tempAssociatedParts)
            product.addAssociatedPart(h);

        try {
            product.inventoryValidCheck();

            Inventory.addProduct(product);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
        //IF INVENTORY IS MORE THAN MAX OR LESS THAN MIN
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Validating New Product");
            alert.setHeaderText("Product not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onActionSearchButton(ActionEvent event) {
        String searchText = addProductSearchField.getText();
        for (Part p : getAllParts()){
            if (Objects.equals(p.getName(), searchText) || Objects.equals(Integer.toString(p.getId()), searchText))
                addPartTableView.getSelectionModel().select(p);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ADDING OBJECTS TO TABLES AND BINDING DATA TO TABLE CELLS
        addPartTableView.setItems(getAllParts());
        addIdTableField.setCellValueFactory(new PropertyValueFactory<>("id"));
        addNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPriceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        addInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productPartsTableView.setItems(tempAssociatedParts);
        productPartIdTableField.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartPriceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPartInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}
