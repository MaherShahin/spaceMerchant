package com.example.guigame;

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

    private ObservableList<Item> currentItemList;

    @FXML
    Item item;
    @FXML
    private Label choicePlayer;
    @FXML
    private Harbor currentHarbor;


    @FXML
    private TableView stockListTable;
    @FXML
    private TableColumn<Item, String> itemName;
    @FXML
    private TableColumn count;
    @FXML
    private TableColumn priceUnit;
    @FXML
    private TableColumn priceMultiplier;
    @FXML
    private TableColumn capacityUnit;



    public HelloController() throws IOException {

    }

    @FXML
    public void initialize() throws IOException {
        Game game = new Game();
        game.newGame();

        List<Harbor> harborsList = game.getPortsList();
        for (Harbor h: harborsList) {
            Button tempButton = new Button(h.getName());
            tempButton.setId(h.getName());
            buttonList.add(tempButton);
        }
        vBox.getChildren().addAll(buttonList);

        for (Button button: buttonList) {
            button.setOnMouseClicked(mouseEvent -> {
                String playerChoiceId = ((Button)mouseEvent.getSource()).getId();
                for (Harbor h: harborsList) {
                    if (h.getName().equals(playerChoiceId)){
                        currentHarbor = h;
                        choicePlayer.setText(playerChoiceId);
                        try {
                            updateTableView();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }

    }

    //TODO -> Figure out how the fuck you use Tableview
    public void setTableView(){

        stockListTable.itemsProperty().set(item);
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityUnit.setCellValueFactory(new PropertyValueFactory<>("unitCapacity"));
        priceUnit.setCellValueFactory(new PropertyValueFactory<>("unitBasePrice"));
    }

    public void updateTableView() throws IOException{
        setTableView();
        ObservableList<Item> items = FXCollections.observableArrayList();
        items = getItems(currentHarbor);
        stockListTable.setItems(items);
    }

    public ObservableList<Item> getItems(Harbor current){
        current = currentHarbor;
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (Map.Entry<Item, Integer> em: current.getStock().entrySet()) {
            items.add(em.getKey());
        }
        return items;
    }


}