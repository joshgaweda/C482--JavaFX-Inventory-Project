/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author gaweda
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.ValidationException;

public class Product 
{
    int productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
 
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
  
    public int getProductId() 
    {
        return productId;
    }
    
    
    public void setProductName(String productName)
    {
        this.productName = productName;
    }    
    
    public String getProductName() 
    {
        return productName;
    }
      
    public void setStock(int inStock) 
    {
        this.productStock = inStock;
    }
    
    public int getStock()  
    {
        return productStock;
    }
       
    public void setMin(int min)
    {
        this.min = min;
    }
    
    public int getMin() 
    {
        return min;
    }

    public void setMax(int max)
    {
        this.max = max;
    }    
    
    public int getMax()
    {
        return max;
    }
    
    public void setProductPrice(double productPrice) 
    {
        this.productPrice = productPrice;
    }    
    
    public double getProductPrice() 
    {
        return productPrice;
    }
    
    public ObservableList<Part> getAllAssociatedParts() 
    {
        return associatedParts;
    }
    
    public int getAssociatedPartsCount()
    {
        return associatedParts.size();
    }
    
    public void addAssociatedParts(Part associatedParts)
    {
        this.associatedParts.add(associatedParts);
    }
    
    
    public Part lookupAssociatedParts(int partID) 
    {
        for (Part a: associatedParts) {
            if (a.getPartId() == partID) {
                return a;
            }
        }
            return null;
    }
    
    public void deleteAllAssociatedParts() 
    {
        associatedParts = FXCollections.observableArrayList();
    }
    
    public boolean deleteAssociatedParts(int partID) 
    {
        for (Part b: associatedParts) {
            if (b.getPartId() == partID) {
                associatedParts.remove(b);
                return true;
            }
        }
        return false;
    }
    
    public boolean isValid() throws ValidationException 
    {
        
        double totalPriceOfParts = 0.00;
        
        for(Part p : getAllAssociatedParts()) 
        {
            totalPriceOfParts += p.getPrice();
        }
        
        if (totalPriceOfParts > getProductPrice()) {
            throw new ValidationException("Product price must be greater than total combined cost of all parts assigned to the product. Please validate prices.");
        }
        
        if (getProductName().equals("")) {
            throw new ValidationException("The name field is required. Please enter a product name."); 
        }
        
        if (getStock() < 0) {
            throw new ValidationException("Inventory must be greater than 0. Please enter a valid amount.");
        }
        
        if (getProductPrice() < 0) {
            throw new ValidationException("Price must be greater than $0. Please enter a valid price.");
        }
        
        if (getMin() < 0) {
            throw new ValidationException("Minimum inventory must be greater than zero. Please enter a valid amount.");
        }
        
        if (getMin() > getMax()) {
            throw new ValidationException("Minimum inventory cannot exceed maximum. Please enter a valid minimum inventory level.");
        }
        
        if (getStock() < getMin() || getStock() > getMax()) {
            throw new ValidationException("Current inventory must be between the minimum and maximum inventory level. Please enter a valid inventory between those values.");
        }
         
        return true;   
    }
     
    
}
