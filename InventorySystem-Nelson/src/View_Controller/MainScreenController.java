package View_Controller;

import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;

public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;
    Part part;
    Product product;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<Part, Integer> partIdTableField;

    @FXML
    private TableColumn<Part, String> partNameTableField;

    @FXML
    private TableColumn<Part, Integer> partInvTableField;

    @FXML
    private TableColumn<Part, Double> priceTableField;

    @FXML
    private TextField partSearchField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productIdTableField;

    @FXML
    private TableColumn<Product, String> productNameTableField;

    @FXML
    private TableColumn<Product, Integer> productInvTableField;

    @FXML
    private TableColumn<Product, Double> productPriceTableField;

    @FXML
    private TextField productSearchField;

    @FXML
    void onActionExitButton(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onActionPartsAddButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onActionPartsDeleteButton(ActionEvent event)  {

        //IF PART IS SELECTED
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            Part selectedPart = partTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "This action will delete Part " + selectedPart.getId() + ". " + "Do you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                getAllParts().remove(selectedPart);
            }
        }
        //IF PART IS NOT SELECTED
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Selection Error");
            alert.setHeaderText("No Part Selected to Delete");
            alert.setContentText("Please Select a Part to Delete");
            alert.showAndWait();
        }
    }

    //SELECTED PART IS PASSED TO setPart() METHOD IN ModifyPartController TO FILL TEXT BOXES IN MODIFY PART SCREEN
    @FXML
    void onActionPartsModifyButton(ActionEvent event) throws IOException {

        //IF PART IS SELECTED
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            Stage stage;
            Parent root;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyPart.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            ModifyPartController controller = loader.getController();
            part = partTable.getSelectionModel().getSelectedItem();
            controller.setPart(part);
        }
        //IF PART IS NOT SELECTED
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Selection Error");
            alert.setHeaderText("No Part Selected For Modification");
            alert.setContentText("Please Select a Part to Modify");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionPartsSearchButton(ActionEvent event) {
        String searchText = partSearchField.getText();
        for (Part p : getAllParts()){
            if (Objects.equals(p.getName(), searchText) || Objects.equals(Integer.toString(p.getId()), searchText))
                partTable.getSelectionModel().select(p);
        }
    }

    @FXML
    void onActionProductsAddButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void onActionProductsDeleteButton(ActionEvent event) {

        //IF PRODUCT IS SELECTED
        if (productTable.getSelectionModel().getSelectedItem() != null) {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "This action will delete Product " + selectedProduct.getId() + ". " + "Do you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                getAllProducts().remove(selectedProduct);
            }
        }
        //IF PRODUCT IS NOT SELECTED
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Selection Error");
            alert.setHeaderText("No Product Selected For Removal");
            alert.setContentText("Please Select a Product to Remove");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionProductsModifyButton(ActionEvent event) throws IOException {

        //IF PRODUCT IS SELECTED
        if (productTable.getSelectionModel().getSelectedItem() != null){
            Stage stage;
            Parent root;
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            ModifyProductController controller = loader.getController();
            product = productTable.getSelectionModel().getSelectedItem();
            controller.setProduct(product);
        }
        //IF NO PRODUCT IS SELECTED
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Selection Error");
            alert.setHeaderText("No Product Selected For Modification");
            alert.setContentText("Please Select a Product to Modify");
            alert.showAndWait();
        }

    }

    @FXML
    void onActionProductsSearchButton(ActionEvent event) {
        String searchText = productSearchField.getText();
        for (Product p : getAllProducts()){
            if (Objects.equals(p.getName(), searchText) || Objects.equals(Integer.toString(p.getId()), searchText))
                productTable.getSelectionModel().select(p);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //SETTING DATA FIELDS TO BE DISPLAYED IN SPECIFIC TABLE VIEW COLUMNS
        partTable.setItems(getAllParts());
        partIdTableField.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productTable.setItems(getAllProducts());
        productIdTableField.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableField.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceTableField.setCellValueFactory(new PropertyValueFactory<>("price"));
        productInvTableField.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }
}
