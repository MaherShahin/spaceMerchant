package com.example.guigame.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Item implements Cloneable, Serializable {
    private String name;
    private int unitCapacity;

    public HashMap<Harbor, Integer> getTradedInHarborPriceMultiplier() {
        return tradedInHarborPriceMultiplier;
    }

    public void setTradedInHarborPriceMultiplier(HashMap<Harbor, Integer> tradedInHarborPriceMultiplier) {
        this.tradedInHarborPriceMultiplier = tradedInHarborPriceMultiplier;
    }

    private int unitBasePrice;
    private HashMap<Harbor,Integer> tradedInHarborPriceMultiplier;
    public Item(String name, int unitCapacity, int unitBasePrice){
        this.setName(name);
        this.setUnitBasePrice(unitBasePrice);
        this.setUnitCapacity(unitCapacity);
        this.tradedInHarborPriceMultiplier = new HashMap<Harbor,Integer>();
    }

    public Item() {
        this.setName(null);
        this.setUnitCapacity(0);
        this.setUnitBasePrice(0);
        this.tradedInHarborPriceMultiplier = new HashMap<Harbor,Integer>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitCapacity() {
        return unitCapacity;
    }

    public void setUnitCapacity(int unitCapacity) {
        this.unitCapacity = unitCapacity;
    }

    public int getUnitBasePrice() {
        return unitBasePrice;
    }

    public void setUnitBasePrice(int unitBasePrice) {
        this.unitBasePrice = unitBasePrice;
    }

    //Manipulation of the table for the items

    //Working correctly
    public void addIsTradedIn(Harbor harbor, Integer multiplier) throws IOException {
        if (multiplier<0){
            throw new IOException("Negative multipliers are not allowed");
        } else {
            this.tradedInHarborPriceMultiplier.put(harbor,multiplier);
        }
    }

    //Working correctly
    public boolean isTradedIn(Harbor harbor) throws IOException {
        if (tradedInHarborPriceMultiplier.isEmpty()){
            throw new IOException("List is empty! I have no clue");
        } else if (!tradedInHarborPriceMultiplier.containsKey(harbor)) {
            throw new IOException("Item doesn't exist in my list :(");
        } else if (tradedInHarborPriceMultiplier.get(harbor).equals(0)){
            return false;
        } else
            return true;
    }

    //Working Correctly
    public int getItemMultiplierAtPort(Harbor harbor) throws IOException {
        if (tradedInHarborPriceMultiplier.isEmpty()){
            throw new IOException("List is empty");
        } else if (!tradedInHarborPriceMultiplier.containsKey(harbor)) {
            throw new IOException("No Such Entry exists!");
        } else if (tradedInHarborPriceMultiplier.get(harbor).equals(0)){
            throw new IOException("Item can't be traded at this port");
        } else {
            return tradedInHarborPriceMultiplier.get(harbor);
        }
    }

    //Working correctly
    public int getPriceIn(Harbor harbor) throws IOException {
        if (tradedInHarborPriceMultiplier.isEmpty()){
            throw new IOException("List is empty");
        } else if (!tradedInHarborPriceMultiplier.containsKey(harbor)){
            throw new IOException("No such entry exists in the tradedInPriceMultiplier Index");
        } else if (tradedInHarborPriceMultiplier.get(harbor).equals(0)){
            return 0;
        } else {
            return tradedInHarborPriceMultiplier.get(harbor) * unitBasePrice;
        }
    }


}
