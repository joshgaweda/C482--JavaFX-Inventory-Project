/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javax.xml.bind.ValidationException;

/**
 *
 * @author gaweda
 */
public abstract class Part
{
    
    int partId;    
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

    public void setPartId(int partId)
    {
        this.partId = partId;
    }
    
    public int getPartId()
    {
        return partId;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    public double getPrice() 
    {
        return price;
    }
    
    public void setInStock(int inStock) 
    {
        this.inStock = inStock;
    }
    
    public int getInStock()
    {
        return inStock;
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
    
    public boolean isValid() throws ValidationException {
     
        if (getName().equals("")) {
            throw new ValidationException("The name field is required. Please enter a name."); 
        }
        
        if (getInStock() < 0) {
            throw new ValidationException("Inventory must be greater than 0. Please enter a valid amount.");
        }        
        
        if (getPrice() < 0) {
            throw new ValidationException("Price must be greater than $0. Please enter a valid price.");
        }
        
        if (getMin() < 0) {
            throw new ValidationException("Minimum inventory must be greater than zero. Please enter a valid amount.");
        }
        
        if (getMin() > getMax()) {
            throw new ValidationException("Minimum inventory cannot exceed maximum. Please enter a valid minimum inventory level.");
        }
        
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("Current inventory must be between the minimum and maximum inventory level. Please enter a valid inventory between those values.");
        }
      return true;  
    }    
}
