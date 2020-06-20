package com.example.retailsupporter.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {
    ArrayList<String> productList;
    ArrayList<String> severProductQuility;
    ArrayList<String> inventoryCountProductQuintity;
    ArrayList<String> userUpdateList;

    public Store() {

    }

    public Store(ArrayList<String> productList, ArrayList<String> severProductQuility, ArrayList<String> inventoryCountProductQuintity, ArrayList<String> userUpdateList) {
        this.productList = productList;
        this.severProductQuility = severProductQuility;
        this.inventoryCountProductQuintity = inventoryCountProductQuintity;
        this.userUpdateList = userUpdateList;
    }

    public ArrayList<String> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<String> productList) {
        this.productList = productList;
    }

    public ArrayList<String> getSeverProductQuility() {
        return severProductQuility;
    }

    public void setSeverProductQuility(ArrayList<String> severProductQuility) {
        this.severProductQuility = severProductQuility;
    }

    public ArrayList<String> getInventoryCountProductQuintity() {
        return inventoryCountProductQuintity;
    }

    public void setInventoryCountProductQuintity(ArrayList<String> inventoryCountProductQuintity) {
        this.inventoryCountProductQuintity = inventoryCountProductQuintity;
    }

    public ArrayList<String> getUserUpdateList() {
        return userUpdateList;
    }

    public void setUserUpdateList(ArrayList<String> userUpdateList) {
        this.userUpdateList = userUpdateList;
    }
}
