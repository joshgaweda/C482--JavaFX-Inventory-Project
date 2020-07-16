package View_Controller;

import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static View_Controller.MainScreenController.getModifiedProduct;
import java.net.URL;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;

public class ModifyProductController implements Initializable 
{
    
    @FXML
    private TextField productIDField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productInventoryField;

    @FXML
    private TextField productPriceField;

    @FXML
    private TextField productMinField;

    @FXML
    private TextField productMaxField;

    @FXML
    private TableView<Part> addPartTable;

    @FXML
    private TableColumn<Part, Integer> addPartPartIDColumn;

    @FXML
    private TableColumn<Part, String> addPartNameColumn;

    @FXML
    private TableColumn<Part,Integer> addPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> addPartPriceColumn;

    @FXML
    private TableView<Part> partsContainedTable;

    @FXML
    private TableColumn<Part, Integer> partsContainedPartIDColumn;

    @FXML
    private TableColumn<Part, String> partsContainedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> partsContainedInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partsContainedPriceColumn;

    @FXML
    private TextField searchPartsField;
    
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    
    private final Product currentModProduct;
    
    public ModifyProductController() {
        this.currentModProduct = getModifiedProduct();
    }
    
   public void populateAvailablePartsTable() {
    addPartTable.setItems(Model.Inventory.getPartInventory());
    }
    
    public void populateCurrentPartsTable() 
    {
        partsContainedTable.setItems(productParts);
    }
    
    @FXML
    void addPartToProductHandler(ActionEvent event) throws IOException {
        Part part = addPartTable.getSelectionModel().getSelectedItem();
        productParts.add(part);
        populateCurrentPartsTable();
    }

    @FXML
    void cancelProductHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modifying Product");
        alert.setHeaderText("Please confirm cancelling modifying product.");
        alert.setContentText("Please confirm you want to cancel update to product " + productNameField.getText() + ".");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
                    
        }
    }

    @FXML
    void deletePartFromProductHandler(ActionEvent event) throws IOException 
    {
        Part part = partsContainedTable.getSelectionModel().getSelectedItem();
        productParts.remove(part);
        populateCurrentPartsTable();
        populateAvailablePartsTable();
        
//        if (productParts.size() > 1) {
//            Part part = partsContainedTable.getSelectionModel().getSelectedItem();
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.initModality(Modality.NONE);
//            alert.setTitle("Part Removal");
//            alert.setHeaderText("Please confirm removal of part from product.");
//            alert.setContentText("Are you sure you want to remove " + part.getName() + "?");
//            Optional<ButtonType> result = alert.showAndWait();   
//            if (result.get() == ButtonType.OK) {
//                productParts.remove(part);
//            }          
//        }
//        
//        else {
//            Part part = partsContainedTable.getSelectionModel().getSelectedItem();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Error deleting part!");
//            alert.setHeaderText("Products require a minimum of one part.");
//            alert.setContentText("All products must have a minimum of one part. Removing this part will delete the product entirely.  Would you like to proceed?");
//            Optional<ButtonType> result = alert.showAndWait();     
//            if (result.get() == ButtonType.OK) 
//            {
//                productParts.remove(part);
//            }  
//        }
    }

    @FXML
    void saveProductHandler(ActionEvent event) throws IOException, ValidationException {
        String productName = productNameField.getText();
        String productInventory = productInventoryField.getText();
        String productPrice = productPriceField.getText();
        String productMin = productMinField.getText();
        String productMax = productMaxField.getText();
        
        if ("".equals(productInventory)) {
            productInventory = "0";
        }
        
        Product newProduct = new Product();
        newProduct.setProductName(productName);
        newProduct.setProductPrice(Double.parseDouble(productPrice));
        newProduct.setStock(Integer.parseInt(productInventory));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
        
        
        
        if (currentModProduct != null) {
            currentModProduct.deleteAllAssociatedParts();
        }
        
        for (Part h: productParts) {
            newProduct.addAssociatedParts(h);
        }
        
        try {
            newProduct.isValid();
        
        
            if (currentModProduct == null) {
                newProduct.setProductId(Inventory.getProductCount());
                Inventory.addProduct(newProduct);
            }
            else {
                newProduct.setProductId(currentModProduct.getProductId());
                Inventory.updateProduct(newProduct);
            }
            
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
            
        }
            catch (ValidationException i) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Validating Product");
                alert.setHeaderText("Product not valid");
                alert.setContentText(i.getMessage());
                alert.showAndWait();
        }   
    }

    @FXML
    void searchPartsButtonHandler (ActionEvent event) throws IOException {
        String partSearchIDString = searchPartsField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partSearchIDString));
        
        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            addPartTable.setItems(filteredPartsList);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Searching");
            alert.setHeaderText("Part not found in inventory.");
            alert.setContentText("The part searched for does not match any current part in Inventory!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {  
            productIDField.setDisable(true);
            productIDField.setText(Integer.toString(currentModProduct.getProductId()));
            productNameField.setText(currentModProduct.getProductName());
            productInventoryField.setText(Integer.toString(currentModProduct.getStock()));
            productPriceField.setText(Double.toString(currentModProduct.getProductPrice()));
            productMinField.setText(Integer.toString(currentModProduct.getMin()));
            productMaxField.setText(Integer.toString(currentModProduct.getMax()));
            
            productParts = currentModProduct.getAllAssociatedParts();
        
        
        addPartPartIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartId()).asObject());
        addPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        addPartInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        addPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        partsContainedPartIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartId()).asObject());
        partsContainedPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partsContainedInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partsContainedPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
       
        populateAvailablePartsTable();
        populateCurrentPartsTable();
        
      
    }

    
}

