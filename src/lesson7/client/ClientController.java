package lesson7.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Date;

public class ClientController {
    @FXML
    private ListView<String> listView;

    private final ObservableList<String> participantList = FXCollections.observableArrayList(
            "Server");

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputField;

    private Network network;

    public void setNetwork(Network network) {
        this.network = network;
    }

    @FXML
    void initialize() {
        listView.setItems(participantList);
    }

    @FXML
    void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isBlank()) {
            try {
                network.getOut().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка при отправке сообщения");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            appendMessage("Я", message);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка ввода сообщения");
            alert.setContentText("Нельзя отправлять пустое сообщение");
            alert.show();
        }
        inputField.clear();
    }

    public synchronized void appendMessage(String who, String message) {
        Object o = new Object();
        synchronized (o) {
            textArea.appendText(String.format("%1$td.%1$tm.%1$tY %1$tT", new Date()) + " @" + who + ": \n");
            textArea.appendText(message + "\n");
        }
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
