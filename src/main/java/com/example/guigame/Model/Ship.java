package com.example.guigame.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Ship implements Cloneable, Serializable {

    //Constructor for the Ship
    public Ship(Captain captain, Harbor startingHarbor){
        this.setCaptain(captain);
        this.fuel = 0;
        this.setMaxCapacity(30);
        this.setTripCounter(0);
        this.setCurrentPort(startingHarbor);
    }

    public Ship(){

    }
    private Captain captain;
    private int capacity;
    private int maxCapacity;
    private int tripCounter;


    public void setCurrentPort(Harbor currentHarbor) {
        this.currentHarbor = currentHarbor;
    }

    public void setShipInventory(HashMap<Item, Integer> shipInventory) {
        this.shipInventory = shipInventory;
    }

    private Harbor currentHarbor;
    private int fuel;
    private HashMap<Item,Integer> shipInventory = new HashMap<Item,Integer>();

    public Captain getCaptain() {
        return captain;
    }

    //Getters and Setters
    public void setCaptain(Captain captain) {
        this.captain = captain;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    public int getTripCounter() {
        return tripCounter;
    }
    public void setTripCounter(int tripCounter) {
        this.tripCounter = tripCounter;
    }
    public void incrementTripCounter(){
        this.tripCounter++;
    }
    public Harbor getCurrentPort() {
        return currentHarbor;
    } //Working as expected
    public int getFuel() {
        return this.fuel;
    }
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }


    //Methods for adding/removing from Inventory
    public void addItemToInventory(Item item, Integer quantity) throws IOException {
        if (this.shipInventory.isEmpty()){
            this.shipInventory.put(item,quantity);
        } else if (!this.shipInventory.containsKey(item)){
            this.shipInventory.put(item,quantity);
        } else if (this.shipInventory.get(item) > 0){
            int oldQuantity = this.shipInventory.get(item);
            this.shipInventory.put(item, oldQuantity+quantity);
        } else {
            this.shipInventory.put(item,quantity);
        }
        this.updateFuel();
        this.updateCapacity();
    }
    public void removeItemFromInventory(Item item,Integer quantity) throws IOException {
        if (this.shipInventory.isEmpty()){
            throw new IOException("Stock is empty, can't remove from empty");
        } else if (!this.shipInventory.containsKey(item)){
            throw new IOException("Item doesn't exist in inventory");
        } else if (this.shipInventory.get(item) - quantity < 0 ){
            throw new IOException("Can't remove this much habibi");
        } else {
            int oldQuantity = this.shipInventory.get(item);
            this.shipInventory.put(item, oldQuantity-quantity);
        }
        this.updateFuel();
        this.updateCapacity();
    }
    public void updateCapacity(){
        int capacity = 0;
        for (Map.Entry me: shipInventory.entrySet()) {
            capacity += ( (Item)me.getKey()).getUnitCapacity() * ((int) me.getValue());
        }
        this.setCapacity(capacity);
    }
    public void updateFuel(){
        int fuel = 0;
        for (Map.Entry me: shipInventory.entrySet()) {
            if ( ((Item)me.getKey()).getName().equals("Treibstoff") ){
                fuel += (int) me.getValue();
            }
        }
        this.setFuel(fuel);
    }
    //Instead of calling update capacity,fuel for sail -> use this removeFuel instead
    public void removeFuel(int quantity){
        for (Map.Entry me: shipInventory.entrySet()) {
            if ( ((Item)me.getKey()).getName().equals("Treibstoff") ){
                int oldFuelValue = (int) me.getValue();
                int newFuelValue = oldFuelValue-quantity;
                me.setValue(newFuelValue);
            }
        }
        updateFuel();
        updateCapacity();
    }

    public HashMap<Item, Integer> getShipInventory() {
        return shipInventory;
    }
    public void printCurrentInventory(){
            if (shipInventory.isEmpty()){
                System.out.println("You have no items in your inventory!");
                System.out.println("-----------------------------------------------------------------------------");

            } else {
                System.out.println("Your Inventory is as such:");
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println(String.format("%-20s", "[item]") + String.format("%-10s" ,"[Count]") + String.format("%-15s", "[Price/Unit]")+ "[Capacity/Unit]");
                for (Map.Entry em: shipInventory.entrySet()){
                    if ((int) em.getValue() > 0) {
                        System.out.println(String.format("%-20s",em.getKey().toString()) + String.format("%-10s" ,em.getValue().toString())
                                + String.format("%-15s",  ((Item)em.getKey()).getUnitBasePrice())
                                + ((Item)em.getKey()).getUnitCapacity());
                    }
                }
                System.out.println("-----------------------------------------------------------------------------");
            }

    }

}
