package View_Controller;

import Model.Inhouse;
import Model.Outsourced;
import Model.Part;
import static Model.Inventory.addPart;
import java.awt.event.MouseAdapter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;        
import javafx.scene.control.RadioButton;


public class AddPartController implements Initializable 
{
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        partIdField.setText("Auto Gen - Disabled");
        partIdField.setDisable(true);
        inHousePartSelect.setSelected(true);
        outsourcedPartSelect.setSelected(false);
        isInHouse = true;
        partSourceFieldLabel.setText("Machine ID");
    } 

    @FXML
    private Label partSourceFieldLabel;

    @FXML
    private TextField partIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partInventoryField;

    @FXML
    private TextField partPriceField;

    @FXML
    private TextField partMinField;

    @FXML
    private TextField partMaxField;

    @FXML
    private TextField partSourceField;
    
    @FXML
    private RadioButton inHousePartSelect;

    @FXML
    private RadioButton outsourcedPartSelect;    
    
    private boolean isInHouse;             
  
    @FXML
    void inHousePartSelectHandler(ActionEvent event) 
    {
        inHousePartSelect.setSelected(true);
        outsourcedPartSelect.setSelected(false);
        isInHouse = true;
        partSourceFieldLabel.setText("Machine ID");
    }
    
    @FXML
    void outsourcedPartSelectHandler(ActionEvent event) 
    {
        outsourcedPartSelect.setSelected(true);
        inHousePartSelect.setSelected(false);
        isInHouse = false;
        partSourceFieldLabel.setText("Company Name");
    }
    
    @FXML
    void partCancelHandler(ActionEvent event) throws IOException 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modifcation of Part");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Please confirm that you want to cancel adding or modifying part " + partNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) 
        {
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    
    @FXML
    void addPartSaveHandler(ActionEvent event) throws IOException 
    {
        String partName = partNameField.getText();
        String partInv = partInventoryField.getText();
        String partPrice = partPriceField.getText();
        String partMin = partMinField.getText();
        String partMax = partMaxField.getText();
        String partSource = partSourceField.getText();
        
        int newPartID = 1;
        for(Part i: Model.Inventory.getPartInventory()) 
        {
            if (i.getPartId() >= newPartID) 
            {
                newPartID = i.getPartId() + 1;
            }
        }
        
        if ("".equals(partInv)) 
        {
            partInv = "0";
        }
        
           if (isInHouse) 
           {
                Inhouse newPart = new Inhouse();
                newPart.setPartId(newPartID);
                newPart.setName(partName);
                newPart.setPrice(Double.parseDouble(partPrice));
                newPart.setInStock(Integer.parseInt(partInv));
                newPart.setMin(Integer.parseInt(partMin));
                newPart.setMax(Integer.parseInt(partMax));
                newPart.setMachineId(Integer.parseInt(partSource));
                try 
                {
                     newPart.isValid();

                     if (newPart.isValid() == true) 
                     {
                        addPart(newPart);
                     }

                     Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                     Scene scene = new Scene(loader);
                     Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                     window.setScene(scene);
                     window.show();

                 } 
                 catch (ValidationException e) 
                 {
                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setTitle("Error Validating Part!");
                     alert.setHeaderText("Part not valid");
                     alert.setContentText(e.getMessage());
                     alert.showAndWait();
                 }  
            } 
            else
            {
                Outsourced newPart = new Outsourced();
                newPart.setPartId(newPartID);
                newPart.setName(partName);
                newPart.setPrice(Double.parseDouble(partPrice));
                newPart.setInStock(Integer.parseInt(partInv));
                newPart.setMin(Integer.parseInt(partMin));
                newPart.setMax(Integer.parseInt(partMax));
                newPart.setCompanyName(partSource);

                try {
                    newPart.isValid();
                    if (newPart.isValid() == true) {
                        addPart(newPart);
                    }

                       Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                 Scene scene = new Scene(loader);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(scene);
                 window.show();


                }
                catch (ValidationException exception) 
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Validating Part!");
                    alert.setHeaderText("Part not valid");
                    alert.setContentText(exception.getMessage());
                    alert.showAndWait();
                 }  
        
            }
        }           
}
