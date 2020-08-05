package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {

        //CREATING SAMPLE DATA TO DISPLAY ON MAIN SCREEN
        Outsourced os1 = new Outsourced(1, "Part1", 24.99, 400, 10, 500, "ACME");
        InHouse ih1 = new InHouse(2, "Part2", 9.99, 12, 1, 15, 441);
        InHouse ih2 = new InHouse(3, "Part3", 4.75, 1000, 1, 5000, 199);
        Outsourced os2 = new Outsourced(4, "Part4", 349.99, 5, 1, 50, "ACME");
        Outsourced os3 = new Outsourced(5, "Part5", 525.00, 1, 1, 1, "ACME");

        //ADDING SAMPLE DATA TO PARTS TABLE VIEW
        Inventory.addPart(os1);
        Inventory.addPart(ih1);
        Inventory.addPart(ih2);
        Inventory.addPart(os2);
        Inventory.addPart(os3);

        //CREATING SAMPLE DATA TO PRODUCTS TABLE VIEW
        Product product1 = new Product(1, "Product One", 499.99, 1, 0, 1);
        Product product2 = new Product(2, "Product Two", 299.99, 1, 0, 1);
        Product product3 = new Product(3, "Product Three", 899.99, 1, 0, 1);
        Product product4 = new Product(4, "Product Four", 699.99, 1, 0, 1);
        Product product5 = new Product(5, "Product Five", 199.99, 1, 0, 1);

        //ADDING SAMPLE DATA TO PRODUCTS TABLE VIEW
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);
        Inventory.addProduct(product5);
        
        launch(args);
    }
}
