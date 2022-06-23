package com.example.spacemerchant.Controller;

import com.example.spacemerchant.Model.Captain;
import com.example.spacemerchant.Model.Game;
import com.example.spacemerchant.Model.Harbor;
import com.example.spacemerchant.Model.Item;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    private Item item;
    private ItemsTableView currentSelectedItem;
    private Game game;

    @FXML
    private VBox vBox;
    private List<Button> harborsButtonList = new ArrayList<>();


    //Player Information
    @FXML
    private Label credits, fuel, shipCapacity,currentPortLabel;

    //Distance To Port
    @FXML
    private ListView distToPorts;


    // Player Inventory / Harbor Stock Tables
    @FXML
    private TableView inventoryListTable,stockListTable;
    @FXML
    private TableColumn itemNameInventory,priceUnitInventory,quantityInventory,capacityUnitInventory;
    @FXML
    private TableColumn itemNameStock,priceUnitStock,quantityStock,capacityUnitStock;
    @FXML
    private Button sellBtn, buyBtn;
    @FXML
    private TextArea sellQuantity, buyQuantity;

    //newGame, SaveBtn, loadBtn
    @FXML
    private Button newGameBtn, saveBtn, loadBtn;



    public Controller() {

    }
    @FXML
    public void initialize() throws IOException {
        startNewGame();
        updateUI();
        initializeAllButtons();
        setSelectedItemStock();
        setSelectedItemInventory();

    }


    public void startNewGame() throws IOException {
        game = new Game();
        game.newGame();

        currentPortLabel.setText(game.getCaptain().getShip().getCurrentPort().getName());
    }
    public void updateUI() throws IOException {

        fuel.setText(Integer.toString(game.getCaptain().getShip().getFuel()));

        shipCapacity.setText(Integer.toString(game.getCaptain().getShip().getCapacity()));
        currentPortLabel.setText(game.getCaptain().getShip().getCurrentPort().getName());
        credits.setText(Integer.toString(game.getCaptain().getCredit()));

        setDistanceToPorts();
        updateHarborBtnColor();
        updateCurrentStock();
        updateCurrentInventory();
        getItemsInventory();

        checkWin();

    }
    public void updateHarborBtnColor(){
        for (Button button: harborsButtonList){
            if (button.getId().equals(game.getCaptain().getShip().getCurrentPort().getName())){
                button.getStyleClass().add("button-active");
            } else {
                button.getStyleClass().clear();
                button.getStyleClass().add("button-normal");
            }
        }

    }


    //Game Functionalities
    public void sail(String playerChoiceHarbor) throws IOException {
        game.getCaptain().sail(playerChoiceHarbor);
        updateUI();
    }
    public void buy() throws IOException,NullPointerException, NumberFormatException {
        try {
            game.getCaptain().buy(currentSelectedItem.getName(), Integer.valueOf(buyQuantity.getText().trim()));
        } catch (IOException | NumberFormatException e) {
            showErrorPopup(e.getMessage());
        }
        updateUI();
        currentSelectedItem = null;
    }
    public void sell() throws Exception {
        try {
            game.getCaptain().sell(currentSelectedItem.getName(), Integer.valueOf(sellQuantity.getText().trim()));
        } catch (Exception e){
            showErrorPopup(e.getMessage());
        }
        updateUI();
        currentSelectedItem = null;

    }
    public void checkWin() throws IOException {
        if (game.getCaptain().getCredit()>600) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Wohooo!");
            alert.setHeaderText("Congratulations you have won");
            alert.setContentText("Would you like to start a new game?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                startNewGame();
                updateUI();
            } else {
                Platform.exit();
            }
        }
    }


    //Initializing the buttons
    public void initializeAllButtons(){
        initializePortButtons();
        initializeNewGameBtn();
        initializeSaveBtn();
        initializeLoadBtn();
        initializeBuyBtn();
        initializeSellBtn();
    }
    public void initializePortButtons(){

        List<Harbor> harborsList = game.getPortsList();
        for (Harbor h: harborsList) {
            Button tempButton = new Button(h.getName());
            tempButton.setId(h.getName());
            harborsButtonList.add(tempButton);
        }

        updateHarborBtnColor();

        vBox.getChildren().addAll(harborsButtonList);
        vBox.setSpacing(7);

        for (Button button: harborsButtonList) {
             button.setMinWidth(100);
            button.setOnMouseClicked(mouseEvent -> {
                String playerChoiceHarbor = ((Button)mouseEvent.getSource()).getId();
                if (playerChoiceHarbor.equals(game.getCaptain().getShip().getCurrentPort().getName())) {
                    return;
                }
                try {
                    sail(playerChoiceHarbor);
                } catch (IOException e) {
                    showErrorPopup(e.getMessage());
                }
            });
        } //Changing the current port when clicking on the button i.e. sailing
    }
    public void initializeBuyBtn(){
        buyBtn.setOnMouseClicked(mouseEvent -> {
            try {
                buy();
            } catch (IOException e) {
                showErrorPopup(e.getMessage());
            } catch (NullPointerException e){
                showErrorPopup("No item selected");
            }
        });
    }
    public void initializeSellBtn(){
        sellBtn.setOnMouseClicked(mouseEvent -> {
            try {
                sell();
            } catch (NullPointerException e){
                showErrorPopup("No item selected");
            } catch (Exception e) {
                showErrorPopup(e.getMessage());
            }
        });
    }
    public void initializeSaveBtn(){
        saveBtn.setOnMouseClicked(mouseEvent->{
            try {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setTitle("Please save your file");
                File file = fileChooser.showSaveDialog(saveBtn.getScene().getWindow());
                if (file!=null){
                    FileOutputStream fileOut = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(game.getCaptain());
                    fileOut.close();
                }
            } catch (Exception e){
                showErrorPopup(e.getMessage());
            }
        });
    }
    public void initializeLoadBtn(){
        loadBtn.setOnMouseClicked(mouseEvent->{
            try {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setTitle("Please save your file");
                File file = fileChooser.showOpenDialog(loadBtn.getScene().getWindow());
                if (file!=null){
                    FileInputStream fileIn = new FileInputStream(file);
                    ObjectInputStream inputStream = new ObjectInputStream(fileIn);
                    Captain captain = (Captain) inputStream.readObject();
                    game.setCaptain(captain);
                    updateUI();
                }
            } catch (Exception e){
                showErrorPopup(e.getMessage());
            }
        });
    }
    public void initializeNewGameBtn(){
        newGameBtn.setOnMouseClicked(mouseEvent->{
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure you wanna do that?");
                alert.setHeaderText("Start a new game");
                if (alert.showAndWait().get() == ButtonType.OK){
                    startNewGame();
                    updateUI();
                } else {
                    return;
                }

            } catch (Exception e){
                showErrorPopup(e.getMessage());
            }
        });
    }
    public static void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    //Updating the distance to other ports list, Selecting Items from it
    public void setDistanceToPorts(){
        distToPorts.getItems().clear();
        try {
            for (Harbor h: game.getCaptain().getShip().getCurrentPort().getDistanceTo().keySet()) {

                String harborName = h.getName();
                Integer distance = game.getCaptain().getShip().getCurrentPort().getDistanceTo(h);
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
    public void updateCurrentInventory() throws IOException {
        setCurrentInventory();
        inventoryListTable.setItems(getItemsInventory());
    }
    public ObservableList<ItemsTableView> getItemsInventory() throws IOException {

        ObservableList<ItemsTableView> items = FXCollections.observableArrayList();
        for (Map.Entry<Item, Integer> em: game.getCaptain().getShip().getShipInventory().entrySet()) {
            if (em.getValue()>0){
                ItemsTableView itemsTableView = new ItemsTableView(em.getKey().getName(),em.getKey().getPriceIn(game.getCaptain().getShip().getCurrentPort()),em.getKey().getUnitCapacity(),em.getValue());
                items.add(itemsTableView);
            }
        }
        return items;
    }
    public void setSelectedItemInventory(){
        inventoryListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldVal, Object newVal) {
                if (inventoryListTable.getSelectionModel().getSelectedItem()!= null){
                    ItemsTableView itemsTableView = (ItemsTableView) inventoryListTable.getItems().get(inventoryListTable.getSelectionModel().getSelectedIndex());
                    currentSelectedItem = itemsTableView;
                    return;
                }
            }
        });
    }


    // Setting and Updating the Stock Table, Selecting Items from it
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
                ItemsTableView itemsTableView = new ItemsTableView(em.getKey().getName(),em.getKey().getPriceIn(game.getCaptain().getShip().getCurrentPort()),em.getKey().getUnitCapacity(),em.getValue());
                items.add(itemsTableView);
            }
        }
        return items;
    }
    public void setSelectedItemStock(){
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

}