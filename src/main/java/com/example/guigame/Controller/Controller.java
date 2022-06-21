package com.example.guigame.Controller;

import com.example.guigame.Model.Game;
import com.example.guigame.Model.Harbor;
import com.example.guigame.Model.Item;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    private Item item;
    private ItemsTableView currentSelectedItem;
    private Game game;

    @FXML
    private VBox vBox;
    private List<Button> buttonList = new ArrayList<>();


    //Player Information
    @FXML
    private Label credits;
    @FXML
    private Label fuel;
    @FXML
    private Label shipCapacity;


    @FXML
    private Label currentPortLabel;
    //Distance To Port
    @FXML
    private ListView distToPorts;



    // Harbor Stock Table
    @FXML
    private TableView stockListTable;
    @FXML
    private TableColumn itemNameStock;
    @FXML
    private TableColumn priceUnitStock;
    @FXML
    private TableColumn quantityStock;
    @FXML
    private TableColumn capacityUnitStock;
    @FXML
    private Button buyBtn;
    @FXML
    private TextArea buyQuantity;



    // Player Inventory Table
    @FXML
    private TableView inventoryListTable;
    @FXML
    private TableColumn itemNameInventory;
    @FXML
    private TableColumn priceUnitInventory;
    @FXML
    private TableColumn quantityInventory;
    @FXML
    private TableColumn capacityUnitInventory;
    @FXML
    private Button sellBtn;
    @FXML
    private TextArea sellQuantity;

    public Controller() {

    }
    @FXML
    public void initialize() throws IOException {
        startNewGame();
        updateUI();
        initializePortButtons();

        getSelectedItemStock();
        getSelectedItemInventory();

        initializeBuyBtn();
        initializeSellBtn();

    }

    public void startNewGame() throws IOException {
        game = new Game();
        game.newGame();

        game.getCaptain().getShip().addItemToInventory((new Item("Treibstoff",1,1)),20);

        currentPortLabel.setText(game.getCaptain().getShip().getCurrentPort().getName());
    }
    public void initializePortButtons(){

        List<Harbor> harborsList = game.getPortsList();
        for (Harbor h: harborsList) {
            Button tempButton = new Button(h.getName());
            tempButton.setId(h.getName());
            buttonList.add(tempButton);
        }

        vBox.getChildren().addAll(buttonList);

        for (Button button: buttonList) {
            button.setOnMouseClicked(mouseEvent -> {
                String playerChoiceHarbor = ((Button)mouseEvent.getSource()).getId();
                if (playerChoiceHarbor.equals(game.getCaptain().getShip().getCurrentPort().getName())) {
                    return;
                }
                try {
                    sailOnBtnClick(playerChoiceHarbor);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } //Changing the current port when clicking on the button i.e. sailing
    }

    public void getSelectedItemStock(){
        stockListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldVal, Object newVal) {
                if (stockListTable.getSelectionModel().getSelectedItem()!= null){
                    ItemsTableView itemsTableView = (ItemsTableView) stockListTable.getItems().get(stockListTable.getSelectionModel().getSelectedIndex());
                    currentSelectedItem = itemsTableView;
                    System.out.println(currentSelectedItem.getName());
                    return;
                }
            }
        });
    }
    public void getSelectedItemInventory(){
        inventoryListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldVal, Object newVal) {
                if (inventoryListTable.getSelectionModel().getSelectedItem()!= null){
                    ItemsTableView itemsTableView = (ItemsTableView) stockListTable.getItems().get(inventoryListTable.getSelectionModel().getSelectedIndex());
                    currentSelectedItem = itemsTableView;
                    return;
                }
            }
        });
    }

    public void updateUI() throws IOException {

        fuel.setText(Integer.toString(game.getCaptain().getShip().getFuel()));

        shipCapacity.setText(Integer.toString(game.getCaptain().getShip().getCapacity()));
//        currentHarbor = ; //Don't remove this value
        currentPortLabel.setText(game.getCaptain().getShip().getCurrentPort().getName());
        credits.setText(Integer.toString(game.getCaptain().getCredit()));

        setDistanceToPorts();
        updateCurrentStock();
        updateCurrentInventory();
        getItemsInventory();
    }

    //Game Functionalities
    public void sailOnBtnClick(String playerChoiceHarbor) throws IOException {
        game.getCaptain().sail(playerChoiceHarbor);
        updateUI();
    }

    public void initializeBuyBtn(){
        buyBtn.setOnMouseClicked(mouseEvent -> {
                    try {
                        game.getCaptain().buy(currentSelectedItem.getName(), Integer.valueOf(buyQuantity.getText()));
                        updateUI();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
    });
    }

    public void initializeSellBtn(){
        sellBtn.setOnMouseClicked(mouseEvent -> {
            try {
                game.getCaptain().sell(currentSelectedItem.getName(), Integer.valueOf(sellQuantity.getText()));
                updateUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //Updating the distance to other ports label
    public void setDistanceToPorts(){
        distToPorts.getItems().clear();
        try {
            for (Harbor h: game.getCaptain().getShip().getCurrentPort().getDistanceTo().keySet()) {
                String harborName = h.getName();
                Integer distance = h.getDistanceTo(game.getCaptain().getShip().getCurrentPort());
                distToPorts.getItems().add(harborName + " " + distance);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Setting and updating the Inventory
    public void setCurrentInventory(){

        inventoryListTable.itemsProperty().set(item);
        itemNameInventory.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityUnitInventory.setCellValueFactory(new PropertyValueFactory<>("unitCapacity"));
        priceUnitInventory.setCellValueFactory(new PropertyValueFactory<>("unitBasePrice"));
        quantityInventory.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void updateCurrentInventory(){
        setCurrentInventory();
        inventoryListTable.setItems(getItemsInventory());
    }

    public ObservableList<ItemsTableView> getItemsInventory(){

        ObservableList<ItemsTableView> items = FXCollections.observableArrayList();
        for (Map.Entry<Item, Integer> em: game.getCaptain().getShip().getShipInventory().entrySet()) {
                ItemsTableView itemsTableView = new ItemsTableView(em.getKey().getName(),em.getKey().getUnitBasePrice(),em.getKey().getUnitCapacity(),em.getValue());
                items.add(itemsTableView);
        }
        return items;
    }


    // Setting and Updating the Stock Table
    public void setCurrentStockTable(){

        stockListTable.itemsProperty().set(item);
        itemNameStock.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityUnitStock.setCellValueFactory(new PropertyValueFactory<>("unitCapacity"));
        priceUnitStock.setCellValueFactory(new PropertyValueFactory<>("unitBasePrice"));
        quantityStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void updateCurrentStock() throws IOException{
        setCurrentStockTable();
        ObservableList<ItemsTableView> items = getItemsHarbor();
        stockListTable.setItems(items);
    }

    public ObservableList<ItemsTableView> getItemsHarbor() throws IOException {
        Harbor current = game.getCaptain().getShip().getCurrentPort();
        ObservableList<ItemsTableView> items = FXCollections.observableArrayList();
        for (Map.Entry<Item, Integer> em: current.getStock().entrySet()) {
            if (em.getValue()>0 && em.getKey().isTradedIn(current)){
                ItemsTableView itemsTableView = new ItemsTableView(em.getKey().getName(),em.getKey().getUnitBasePrice(),em.getKey().getUnitCapacity(),em.getValue());
                items.add(itemsTableView);
            }
        }
        return items;
    }


}