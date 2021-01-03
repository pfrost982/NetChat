package net_chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {

    private final ObservableList<String> wordList = FXCollections.observableArrayList("Новый год", "Праздник", "Подарки",
            "Елка", "Украшения");

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField inputField;

    @FXML
    void initialize() {
        listView.setItems(wordList);
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String message = inputField.getText().trim();
        if (!message.isBlank()) {
            addMessageToList(message);
        }
        inputField.clear();
    }

    private void addMessageToList(String message) {
        listView.getItems().add(message);
    }

    @FXML
    void showAbout(ActionEvent event) {
        System.out.println("about");
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }
}
