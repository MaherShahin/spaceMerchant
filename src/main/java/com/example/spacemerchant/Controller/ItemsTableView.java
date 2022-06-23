package com.example.spacemerchant.Controller;

public class ItemsTableView {

    // Used for casting the items into an appropriate class that is insertable into tableview for stock&inventory

    String name;
    Integer unitBasePrice;
    Integer unitCapacity;
    Integer quantity;

    public ItemsTableView(String name, Integer unitBasePrice, Integer unitCapacity, Integer quantity) {
        this.name = name;
        this.unitBasePrice = unitBasePrice;
        this.unitCapacity = unitCapacity;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnitBasePrice() {
        return unitBasePrice;
    }

    public void setUnitBasePrice(Integer unitBasePrice) {
        this.unitBasePrice = unitBasePrice;
    }

    public Integer getUnitCapacity() {
        return unitCapacity;
    }

    public void setUnitCapacity(Integer unitCapacity) {
        this.unitCapacity = unitCapacity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
