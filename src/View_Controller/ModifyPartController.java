package View_Controller;

import Model.Inhouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import static View_Controller.MainScreenController.getModifyPart;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

public class ModifyPartController implements Initializable 
{
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {

        partIdField.setDisable(true);
        partIdField.setText(Integer.toString(modifyPart.getPartId()));
        partNameField.setText(modifyPart.getName());
        partInventoryField.setText(Integer.toString(modifyPart.getInStock()));
        partPriceField.setText(Double.toString(modifyPart.getPrice()));
        partMinField.setText(Integer.toString(modifyPart.getMin()));
        partMaxField.setText(Integer.toString(modifyPart.getMax()));

      if (modifyPart instanceof Inhouse) {
            partSourceField.setText(Integer.toString(((Inhouse) modifyPart).getMachineId()));

            partSourceFieldLabel.setText("Machine ID");
            inhousePartSelect.setSelected(true);

        } else {
            partSourceField.setText(((Outsourced) modifyPart).getCompanyName());
            partSourceFieldLabel.setText("Company Name");
            outsourcedPartSelect.setSelected(true);

        }
    }    

    @FXML
    private RadioButton inhousePartSelect;

    @FXML
    private RadioButton outsourcedPartSelect;

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

    private boolean isInHouse;   
    
    private final Part modifyPart;
    
    public ModifyPartController() 
    {
      this.modifyPart = getModifyPart();
    }

    
    @FXML
    void inhousePartSelectHandler(ActionEvent event) 
    {
        isInHouse = true;
        partSourceFieldLabel.setText("Machine ID");
    }
    
    @FXML
    void outsourcedPartSelectHandler(ActionEvent event) {
        isInHouse = false;
        partSourceFieldLabel.setText("Company Name");
   }

    @FXML
    void partCancel(ActionEvent event) throws IOException{

    }

    @FXML
    void partCancelHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modifcation of Part");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Please confirm that you want to cancel modifying part " + partNameField.getText() + "?");
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
    void modPartSaveHandler(ActionEvent event) throws IOException {
        String partID    = partIdField.getText();
        String partName  = partNameField.getText();
        String partInv   = partInventoryField.getText();
        String partPrice = partPriceField.getText();
        String partMin   = partMinField.getText();
        String partMax   = partMaxField.getText();
        String partFlex  = partSourceField.getText();
        
        if ("".equals(partInv)) {
            partInv = "0";
        }
        
           if (isInHouse) {
           Inhouse modifyPart = new Inhouse();
           modifyPart.setPartId(Integer.parseInt(partID));
           modifyPart.setName(partName);
           modifyPart.setPrice(Double.parseDouble(partPrice));
           modifyPart.setInStock(Integer.parseInt(partInv));
           modifyPart.setMin(Integer.parseInt(partMin));
           modifyPart.setMax(Integer.parseInt(partMax));
           modifyPart.setMachineId(Integer.parseInt(partFlex));

           try {
               modifyPart.isValid();
               
               if (modifyPart.isValid() == true) {
                    Inventory.updatePart(modifyPart);                 
               }
             
                  Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
               
            } catch (ValidationException exception) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Validating Part!");
                alert.setHeaderText("Part not valid");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
                }  
            } 
        else {
           Outsourced modifyPart = new Outsourced();
           modifyPart.setPartId(Integer.parseInt(partID));
           modifyPart.setName(partName);
           modifyPart.setPrice(Double.parseDouble(partPrice));
           modifyPart.setInStock(Integer.parseInt(partInv));
           modifyPart.setMin(Integer.parseInt(partMin));
           modifyPart.setMax(Integer.parseInt(partMax));
           modifyPart.setCompanyName(partFlex);

           try {
               modifyPart.isValid();
               if (modifyPart.isValid() == true) {
                   Inventory.updatePart(modifyPart);
        
               }
               
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
             
           } catch (ValidationException exception) {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Error Validating Part!");
               alert.setHeaderText("Part not valid");
               alert.setContentText(exception.getMessage());
               alert.showAndWait();
               }  
        
            }
       
    }
}
    

