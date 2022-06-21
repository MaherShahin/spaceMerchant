package com.example.guigame.Controller;

import com.example.guigame.Model.Game;
import com.example.guigame.Model.Harbor;
import com.example.guigame.Model.Item;
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

public class HelloController {

    @FXML
    private VBox vBox;
    private List<Button> buttonList = new ArrayList<>();

    @FXML
    private Label credits;
    @FXML
    private Label fuel;
    @FXML
    private Label shipCapacity;
    @FXML
    private ListView distToPorts;
    @FXML
    Item item;

    @FXML
    private Harbor currentHarbor;
    @FXML
    private TableView stockListTable;
    @FXML
    private TableColumn<Item, String> itemName;
    @FXML
    private TableColumn priceUnit;
    @FXML
    private TableColumn quantity;
    @FXML
    private TableColumn capacityUnit;
    Game game;
    @FXML
    private Label currentPortLabel;

    public HelloController() {

    }
    @FXML
    public void initialize() throws IOException {
        game = new Game();
        game.newGame();
        game.getCaptain().getShip().addItemToInventory((new Item("Treibstoff",1,1)),20);
        currentHarbor = game.getCaptain().getShip().getCurrentPort();
        currentPortLabel.setText(currentHarbor.getName());

        updatePlayerInfo();
        initializePortButtons();

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
                if (playerChoiceHarbor.equals(currentHarbor.getName())) {
                    return;
                }
                try {
                    game.getCaptain().sail(playerChoiceHarbor);
                    fuel.setText(Integer.toString(game.getCaptain().getShip().getFuel()));
                    currentHarbor = game.getCaptain().getShip().getCurrentPort();
                    currentPortLabel.setText(playerChoiceHarbor);
                    setDistanceToPorts();
                    updateCurrentStock();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } //Changing the current port when clicking on the button i.e. sailing
    }



    public void setDistanceToPorts(){
        distToPorts.getItems().clear();
        try {
            for (Harbor h: currentHarbor.getDistanceTo().keySet()) {
                String harborName = h.getName();
                Integer distance = h.getDistanceTo(currentHarbor);
                distToPorts.getItems().add(harborName + " " + distance);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePlayerInfo(){

        credits.setText(Integer.toString(game.getCaptain().getCredit()));
        shipCapacity.setText(Integer.toString(game.getCaptain().getShip().getCapacity()));
        fuel.setText(Integer.toString(game.getCaptain().getShip().getFuel()));

    }

    // Since TableView needs all inputs to be of the same type, but Item doesn't have the variable quantity in it.
    // My idiotic solution - that works for now - is just creating a new class ItemsTableView which takes the values
    // from each item and puts it into the new ItemsTableView as well as the quantity, then put the Object directly into
    // the Tableview... I feel stupid just writing this..
    public void setCurrentStockTable(){

        stockListTable.itemsProperty().set(item);
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityUnit.setCellValueFactory(new PropertyValueFactory<>("unitCapacity"));
        priceUnit.setCellValueFactory(new PropertyValueFactory<>("unitBasePrice"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void updateCurrentStock() throws IOException{
        setCurrentStockTable();
        ObservableList<ItemsTableView> items = getItems();
        stockListTable.setItems(items);
    }

    public ObservableList<ItemsTableView> getItems(){
        Harbor current = currentHarbor;
        ObservableList<ItemsTableView> items = FXCollections.observableArrayList();
        for (Map.Entry<Item, Integer> em: current.getStock().entrySet()) {
            ItemsTableView itemsTableView = new ItemsTableView(em.getKey().getName(),em.getKey().getUnitBasePrice(),em.getKey().getUnitCapacity(),em.getValue());
            items.add(itemsTableView);
        }
        return items;
    }


}