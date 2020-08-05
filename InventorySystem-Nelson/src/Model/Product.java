package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.ValidationException;

public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAsPart){
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    //CHECKS IF ENTERED INVENTORY IS WITHIN MIN AND MAX
    public boolean inventoryValidCheck() throws ValidationException {

       int tempMin = getMin();
       int tempMax = getMax();
       int tempStock = getStock();

       if (tempStock > tempMax) {
           throw new ValidationException("Inventory cannot be larger than max.");
       }

       if (tempStock < tempMin) {
           throw new ValidationException("Inventory cannot be smaller than min.");
       }

       return true;
    }
}
