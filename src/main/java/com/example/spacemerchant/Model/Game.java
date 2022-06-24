package com.example.spacemerchant.Model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game implements Cloneable, Serializable {

    private static List<Harbor> portsList;
    private static List<Item> itemsList;
    private static Captain captain;
    private static Ship ship;

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }
    public Captain getCaptain() {
        return captain;
    }
    public Game() {
        itemsList = new ArrayList<Item>();
        portsList = new ArrayList<Harbor>();
    }
    public static void newGame() throws IOException {
        new Game();
        initializeItemsAndPorts();
        captain = new Captain();
        ship = new Ship(captain,portsList.get(0));

        captain.setShip(ship);
    }
    public static void initializeItemsAndPorts() throws IOException {
        //Items
        Item polymer = new Item("Polymer",2,20 );
        Item computer = new Item("Computer",1,50);
        Item kalaxianCrystals = new Item("Kalaxian Crystals",4,10);
        Item fuel = new Item("Fuel",1,1);
        Item quantumFluid = new Item("Quantum Fluid",3,30);
        Item neon = new Item("Neon",2,15);
        Item plumbus = new Item("Plumbus",3,30);
        Item portalGun = new Item("Portal Gun", 1,70);
        Item deathCrystal = new Item("Death Crystal", 1, 25);

        //List of Ports

        Harbor tortuga = new Harbor("Tortuga");
        Harbor alexandria = new Harbor("Alexandria");
        Harbor cairo = new Harbor("Cairo");
        Harbor athens = new Harbor("Athens");
        Harbor luxor = new Harbor("Luxor");
        Harbor thessaloniki = new Harbor("Thessaloniki");


        itemsList.add(neon);
        itemsList.add(quantumFluid);
        itemsList.add(fuel);
        itemsList.add(kalaxianCrystals);
        itemsList.add(computer);
        itemsList.add(polymer);
        itemsList.add(plumbus);
        itemsList.add(portalGun);
        itemsList.add(deathCrystal);


        portsList.add(tortuga);
        portsList.add(alexandria);
        portsList.add(cairo);
        portsList.add(athens);
        portsList.add(thessaloniki);
        portsList.add(luxor);

        randomizeItemsAndPorts();
    }
    public static void randomizeItemsAndPorts() throws IOException {
        Random rand = new Random();

        //Assign for every item a random value for isTradedIn between (4-0) for PriceMultiplier + Tradeability
        for (Item i: itemsList){
            for (Harbor p: portsList) {
                if (i.getName().toLowerCase().equals("treibstoff")){
                    i.addIsTradedIn(p,1);
                } else {
                i.addIsTradedIn(p,rand.nextInt(4));
                }
            }
        }

        //Assign for every port a random value of items in stock
        for (Harbor p: portsList) {
            for (Item i: itemsList) {
                p.addItemToStock(i,rand.nextInt(20));
            }
        }

        //Assign for every port a rand dist to the other ports
        for (Harbor p1: portsList) {
            for (Harbor p2: portsList) {
                if (!p1.getName().equals(p2.getName())){
                    p1.addDistanceTo(p2, rand.nextInt(3)+1);
                }
            }
        }
    }
    public void printGameData() throws IOException {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Current credits: "+ this.getCaptain().getCredit());
        System.out.println("Current Trip counter: " + this.getCaptain().getShip().getTripCounter());
        System.out.println("Current fuel is: "+this.getCaptain().getShip().getFuel());
        System.out.println("-----------------------------------------------------------------------------");
        this.getCaptain().getShip().printCurrentInventory();
        System.out.println("Current port is: " + this.getCaptain().getShip().getCurrentPort().toString());
        System.out.println("-----------------------------------------------------------------------------");
        this.getCaptain().getShip().getCurrentPort().printAllItemsInStock();
    }

    public List<Harbor> getPortsList() {
        return portsList;
    }

    public void saveGameBinary(String filename){
        try{
            File file = new File(filename);
            String currentDir = System.getProperty("user.dir");
            FileOutputStream fileOut = new FileOutputStream(currentDir+"\\gameSaves\\"+file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(captain);
            fileOut.close();
            System.out.println("Serialized data is saved in " + filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadGameBinary(String filename) {
        try {
            File file = new File(filename);
            String currentDir = System.getProperty("user.dir");
            FileInputStream fileIn = new FileInputStream(currentDir+"\\gameSaves\\"+file);
            ObjectInputStream inputStream = new ObjectInputStream(fileIn);
            Captain captain = (Captain) inputStream.readObject();
            setCaptain(captain);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGameXML(String filename) throws FileNotFoundException {
        XMLEncoder encoder = null;
        File file = new File(filename);
        String currentDir = System.getProperty("user.dir");
        try {
            FileOutputStream fileOut = new FileOutputStream(currentDir+"\\gameSaves\\"+file);
            encoder = new XMLEncoder(fileOut);
            encoder.writeObject(captain);
            encoder.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPortsList(List<Harbor> portsList) {
        portsList = portsList;
    }

    public static List<Item> getItemsList() {
        return itemsList;
    }

    public static void setItemsList(List<Item> itemsList) {
        itemsList = itemsList;
    }

    public static Ship getShip() {
        return ship;
    }

    public static void setShip(Ship ship) {
        ship = ship;
    }

    public void loadGameXML(String filename) {
        File file = new File(filename);
        String currentDir = System.getProperty("user.dir");
        XMLDecoder decoder = null;
        try {
            FileInputStream inputStream = new FileInputStream(currentDir + "\\gameSaves\\" + file);
            decoder = new XMLDecoder(inputStream);
            Captain captain = (Captain) decoder.readObject();
            setCaptain(captain);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
