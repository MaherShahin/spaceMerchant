package com.example.spacemerchant.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Harbor implements Cloneable, Serializable {

    public Harbor(String name){
        this.setName(name);
    }

    public HashMap<Harbor, Integer> getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(HashMap<Harbor, Integer> distanceTo) {
        this.distanceTo = distanceTo;
    }

    public void setStock(HashMap<Item, Integer> stock) {
        this.stock = stock;
    }

    public Harbor(){
        this.setName(null);
    }
    private String name;
    private HashMap<Harbor,Integer> distanceTo = new HashMap<Harbor,Integer>();
    private HashMap<Item, Integer> stock = new HashMap<Item,Integer>();

    //Setters and Getters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Item, Integer> getStock() {
        return stock;
    }

    //Overrides for equals and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Harbor harbor = (Harbor) o;
        return Objects.equals(name, harbor.name) && Objects.equals(distanceTo, harbor.distanceTo) && Objects.equals(stock, harbor.stock);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    //Stock Manipulation
    public void addItemToStock(Item item, Integer quantity) throws IOException {
        if (stock.isEmpty()){
            stock.put(item,quantity);
        } else if (!stock.containsKey(item)){
            stock.put(item,quantity);
        } else if (stock.get(item) > 0){
            int oldQuantity = stock.get(item);
            stock.put(item, oldQuantity+quantity);
        } else {
            stock.put(item,quantity);
        }
    }
    public void removeItemFromStock(Item item, Integer quantity) throws IOException {
        if (stock.isEmpty()){
            throw new IOException("Stock is Empty");
        } else if (!stock.containsKey(item)){
            throw new IOException("Item Doesn't Exist in Stock!");
        } else if (stock.get(item) - quantity < 0){
            throw new IOException("Not enough items in stock");
        } else {
            int oldQuantity = stock.get(item);
            stock.put(item,oldQuantity-quantity);
        }
    }
    public HashMap<Harbor, Integer> getDistanceToHashMap() {
        return distanceTo;
    }
    public void printAllItemsInStock() throws IOException {
        if (stock.isEmpty()){
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("We have no items here :(");
            System.out.println("-----------------------------------------------------------------------------");

        } else {
            System.out.println("Welcome to " + this.getName() + ", Here's our available stock: ");
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println(String.format("|"+"%-20s", "[item]") + String.format("%-10s" ,"[Count]") + String.format("%-15s", "[Price/Unit]")
                    + String.format("%-15s", "[Multiplier]")+ String.format("%-15s","[Capacity/Unit]") +"|");
            for (Map.Entry em: stock.entrySet()){
                if ((int) em.getValue() > 0 && ((Item) em.getKey()).isTradedIn(this)) {
                    System.out.println(String.format("|"+"%-20s",em.getKey().toString()) + String.format("%-10s" ,em.getValue().toString())
                             + String.format("%-15s",  ((Item)em.getKey()).getPriceIn(this))
                            +String.format("%-15s", (((Item) em.getKey()).getItemMultiplierAtPort(this)))+ ((Item)em.getKey()).getUnitCapacity()+ String.format("%15s", "|"));
                }
            }
            System.out.println("-----------------------------------------------------------------------------");

        }
    }

    //DistanceTo Hash Map manipulation
    public void addDistanceTo(Harbor harbor, Integer dist) throws IOException {
        if (dist<0){
            throw new IOException("Can't have negative distances");
        } else {
            this.distanceTo.put(harbor,dist);
        }
    }
    public int getDistanceTo(Harbor harbor) throws IOException {
        if (distanceTo.isEmpty()){
            throw new IOException("Can't get from an empty list anything");
        } else if(!distanceTo.containsKey(harbor)){
            throw new IOException("No info about this specific port my dude");
        } else {
            return distanceTo.get(harbor);
        }
    }

    //Getting items/ports for a string input -> used to process the input of the user
    public Item getItemByStringName(String name){
        Item item = new Item();
        for (Map.Entry em: this.getStock().entrySet()) {
            if ( ((Item)em.getKey()).getName().toLowerCase().equals(name.toLowerCase()) ){
                item = (Item)em.getKey();
            }
        }
        return item;
    }
    public Harbor getPortByPortName(String name){
        Harbor harbor = new Harbor();
        for (Map.Entry em: this.distanceTo.entrySet()) {
            if ( ((Harbor)em.getKey()).getName().toLowerCase().equals(name.toLowerCase()) ){
                harbor = (Harbor)em.getKey();
            }
        }
        return harbor;
    }

}
