package net_chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;

public class Controller {
    @FXML
    private ListView<String> listView;

    private final ObservableList<String> participantList = FXCollections.observableArrayList(
            "Александр", "Георгий", "Юрий", "Елена", "Ксения");

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputField;

    @FXML
    void initialize() {
        listView.setItems(participantList);
    }

    @FXML
    void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isBlank()) {
            textArea.appendText(String.format("%1$td.%1$tm.%1$tY %1$tT", new Date()) + " @Я: \n");
            textArea.appendText(message + "\n");

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка ввода сообщения");
            alert.setContentText("Нельзя отправлять пустое сообщение");
            alert.show();
        }
        inputField.clear();
    }

    @FXML
    void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Сетевой чат");
        alert.setContentText("В будующем из этого получится сетевой чат, надеюсь...)))");
        alert.show();
    }

    @FXML
    void delete() {
        textArea.clear();
    }

    @FXML
    void exit() {
        System.exit(0);
    }
}
