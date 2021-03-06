package gypfxml;

import gypfxml.ui.ScreenResource;
import gypfxml.core.Inventory;
import gypfxml.core.Part;
import gypfxml.core.Product;
import gypfxml.test.Test;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import gypfxml.ui.ScreenController;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class App extends Application {
    
    private static App instance;
    
    private Inventory inventory;
    
    private Part  selectedPart;
    private Product selectedProduct;
    
    private Stage stage;
    private Map<String, Scene> scenes;
    private Map<String, ScreenController> screenControllers;
    
    private Alert alert;
    private Alert confirmation;
    
    private int nextPartId;
    private int nextProductId;
    
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;
        alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error Dialog");
        confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirmation Dialog");
        stage.setTitle("Inventory Management Application");
        scenes = new HashMap<>();
        screenControllers = new HashMap<>();
        selectedPart = null;
        selectedProduct = null;
        nextPartId = 1;
        nextProductId = 1;
        inventory = new Inventory();
        Test.addRandomParts(this, 100);
        Test.addRandomProducts(this, inventory.getParts(), 10);
        showScreen(ScreenResource.MAIN);
        stage.show();
    }
    
    public void showScreen(String screenResource) {
        showScreen(screenResource, true);
    }
    
    public void showScreen(String screenResource, boolean toRefresh) {
        Scene scene = scenes.get(screenResource);
        if (scene == null) {
            URL rsc = getClass().getResource(screenResource);
            try {
                Parent parent = FXMLLoader.load(rsc);
                scene = new Scene(parent);
                scenes.put(screenResource, scene);
            } catch (IOException e) {
                System.err.println(e);
            }
        } else if (toRefresh) {
            screenControllers.get(screenResource).refresh();
        }
        this.stage.setScene(scene);
    }
    
    public void setScreenController(String screenResource, ScreenController screenController) {
        screenControllers.put(screenResource, screenController);
    }
    
    public FilteredList<Part> initPartTable(TableView<Part> table) {
        return initPartTable(table, inventory.getParts());
    }
    
    public FilteredList<Part> initPartTable(TableView<Part> table, ObservableList<Part> list) {
        return initTable(table, list, "partID");
    }
    
    public FilteredList<Product> initProductTable(TableView<Product> table) {
        return initTable(table, inventory.getProducts(), "productID");
    }
    
    private <T> FilteredList<T> initTable(TableView<T> table, ObservableList<T> list, String idPropertyName) {
        List<TableColumn<T, ?>> columns = table.getColumns();
        
        TableColumn<Product, Integer> idCol = (TableColumn<Product, Integer>) columns.get(0);
        idCol.setCellValueFactory(new PropertyValueFactory<>(idPropertyName));
        
        TableColumn<Product, String> nameCol = (TableColumn<Product, String>) columns.get(1);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Integer> invCol = (TableColumn<Product, Integer>) columns.get(2);
        invCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        
        TableColumn<Product, Double> priceCol = (TableColumn<Product, Double>) columns.get(3);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        FilteredList<T> filteredList = new FilteredList<>(list, p -> true);
        table.setItems(filteredList);
        return filteredList;
    }
    
    public void displayError(String error) {
        alert.setContentText(error);
        alert.showAndWait();
    }
    
    public boolean displayConfirmation(String message) {
        confirmation.setContentText(message);
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.get()== ButtonType.OK;
    }
    
    public void addPart(Part part) {
        part.setPartID(nextPartId++);
        inventory.addPart(part);
    }
    
    public void addProduct(Product product) {
        product.setProductID(nextProductId++);
        inventory.addProduct(product);
    }
    
    public void updatePart(Part part) {
        inventory.updatePart(part);
    }
    
    public Product getSelectedProduct() {
        return selectedProduct;
    }
    
    public Part getSelectedPart() {
        return selectedPart;
    }
    
    public void filterParts(String keyword, FilteredList<Part> list) {
        String normalizedKeyword = keyword.trim().toLowerCase();
        if (normalizedKeyword.length() == 0) {
            list.setPredicate(part -> true);
        } else {
            list.setPredicate(part -> part.getName().toLowerCase().contains(normalizedKeyword));
        }
    }
    
    public void filterProducts(String keyword, FilteredList<Product> list) {
        String normalizedKeyword = keyword.trim().toLowerCase();
        if (normalizedKeyword.length() == 0) {
            list.setPredicate(product -> true);
        } else {
            list.setPredicate(product -> product.getName().toLowerCase().contains(normalizedKeyword));
        }
    }
    
    public void modifyPart(Part selectedPart) {
        this.selectedPart = selectedPart;
        showScreen(ScreenResource.MODIFY_PART);
    }
    
    public void modifyProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        showScreen(ScreenResource.MODIFY_PRODUCT);
    }
    
    public boolean deletePart(Part part) {
        return inventory.deletePart(part);
    }
    
    public boolean deleteProduct(Product product) {
        return inventory.removeProduct(product.getProductID());
    }
    
    public static App getInstance() {
        return instance;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
