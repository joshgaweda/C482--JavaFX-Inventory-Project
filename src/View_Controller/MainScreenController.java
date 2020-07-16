package View_Controller;


import Inventory.Main;
import Model.Part;
import Model.Product;
import static Model.Inventory.getAllProducts;
import static Model.Inventory.canDeleteProduct;
import static Model.Inventory.deletePart;
import static Model.Inventory.deleteProduct;
import static Model.Inventory.getPartInventory;

import java.io.IOException;
import java.util.Optional;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MainScreenController implements Initializable 
{

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    { 
        setModifyPart(null);
        setModifiedProduct(null);
        
        mainPartIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartId()).asObject());
        mainPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        mainPartInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        mainPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        mainProductIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());
        mainProductNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        mainProductInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        mainProductPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProductPrice()).asObject());
        
        refreshPartsTable();
        refreshProductsTable();   
    }        
    
    @FXML
    private TableView<Part> mainPartsTable;

    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;

    @FXML
    private TableColumn<Part, String> mainPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> mainPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> mainPartPriceColumn;

    @FXML
    private TextField partsSearchBox;

    @FXML
    private TableView<Product> mainProductTable;

    @FXML
    private TableColumn<Product, Integer> mainProductIDColumn;

    @FXML
    private TableColumn<Product, String> mainProductNameColumn;

    @FXML
    private TableColumn<Product, Integer> mainProductInventoryColumn;

    @FXML
    private TableColumn<Product, Double> mainProductPriceColumn;

    @FXML
    private TextField productSearchBox;
   
    private static Part currentModPart;
    
    private static Product currentModProduct;

    @FXML
    void addPartMainHandler(ActionEvent event) throws IOException  {
        openAddPartWindow(event);
    }
    
      @FXML
    void modPartMainHandler(ActionEvent event) throws IOException  {
        openModifyPartWindow(event);
    }

    @FXML
    void addProductMainHandler(ActionEvent event) throws IOException  {
        openAddProductScreen(event);
    }
    
    @FXML
    void deletePartHandler(ActionEvent event) throws IOException  {
        Part part = mainPartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Please Confirm Deletion");
        alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            deletePart(part.getPartId());
            refreshPartsTable();
    }
}
    
    @FXML
    void deleteProductHandler(ActionEvent event) throws IOException  
    {
        Product product = mainProductTable.getSelectionModel().getSelectedItem();
        if (!canDeleteProduct(product)) 
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("ERROR IN DELETING PRODUCT");
            alert.setHeaderText("This product cannot be removed");
            alert.setContentText("This product has associated parts. Please disassociate those parts or delete those parts, then try again.");
            alert.showAndWait();
        }
        else 
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("DELETING PRODUCT");
            alert.setHeaderText("Please confirm deletion.");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) 
            {
                deleteProduct(product.getProductId());
                refreshPartsTable();
            }
        }
    }
    
    @FXML
    void exitApplicationHandler(ActionEvent event) throws IOException  
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exiting Confirmation");
        alert.setHeaderText("Please confirm that you want to exit!");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) 
        {
            System.exit(0);
        }        
    }

    @FXML
    void modifyPartMainHandler(ActionEvent event) throws IOException  
    {
        currentModPart = mainPartsTable.getSelectionModel().getSelectedItem();
        setModifyPart(currentModPart);
        openModifyPartWindow(event);
    }

    @FXML
    void mainModifyProductHandler(ActionEvent event) throws IOException  
    {
        currentModProduct = mainProductTable.getSelectionModel().getSelectedItem();
        setModifiedProduct(currentModProduct);
        openModifyProductScreen(event);
            
    }

    @FXML
    void searchPartsHandler(ActionEvent event) throws IOException  
    {
        Model.Inventory.getPartInventory().stream().filter((i) -> (i.getName().equalsIgnoreCase(partsSearchBox.getText()))).forEachOrdered((i) -> {
            mainPartsTable.getSelectionModel().select(i);
        });
    }

    @FXML
    void searchProductsHandler(ActionEvent event) throws IOException 
    {
        Model.Inventory.getAllProducts().stream().filter((i) -> (i.getProductName().equalsIgnoreCase(productSearchBox.getText()))).forEachOrdered((i) -> {
            mainProductTable.getSelectionModel().select(i);
        });
    }
        
    public void openAddPartWindow(ActionEvent event) throws IOException 
    {
        Parent loader = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
     
    public void openModifyPartWindow(ActionEvent event) throws IOException 
    {
        if (currentModPart != null)
        {
            Parent loader = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                   alert.setTitle("You have not selected a Part to Modify.");
                   alert.setHeaderText("Please select a Part to Modify");
                   alert.setContentText("Please click okay to return to main screen.");
                   alert.showAndWait();
        }  
    }
    
    public void openAddProductScreen(ActionEvent event) throws IOException 
    {
        Parent loader = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
     
    public void openModifyProductScreen(ActionEvent event) throws IOException 
    {
         if (currentModProduct != null)
         {
              Parent loader = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
              Scene scene = new Scene(loader);
              Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
              window.setScene(scene);
              window.show();
          }
          else    
          {
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setTitle("You have not selected a Product to Modify.");
                     alert.setHeaderText("Please select a Product to Modify");
                     alert.setContentText("Please click okay to return to main screen.");
                     alert.showAndWait();
          }  
     }
    
    public static Part getModifyPart()
    {
        return currentModPart;
    }

    public void setModifyPart(Part modifyPart) 
    {
        View_Controller.MainScreenController.currentModPart = modifyPart;
    }
    
    public static Product getModifiedProduct()  
    {
        return currentModProduct;
    }
  
    public void setModifiedProduct(Product modifiedProduct) 
    {
        MainScreenController.currentModProduct = modifiedProduct;
    }
    
    public void refreshPartsTable()
    {
        mainPartsTable.setItems(getPartInventory());
    }
   
    public void refreshProductsTable()
    {
        mainProductTable.setItems(getAllProducts());
    }
    
    public void setApp(Main mainApp)
    {
        refreshPartsTable();
        refreshProductsTable();
    }
}
    

