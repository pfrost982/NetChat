package lesson7_8.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Date;

public class ChatController {
    private Network network;
    private String username;

    private ObservableList<String> participantList = FXCollections.observableArrayList(
            "Пусто");

    private String selectedRecipient;

    @FXML
    private ListView<String> listView;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField inputField;

    @FXML
    void initialize() {
        listView.setItems(participantList);
//-----------------------------неведомая хня
        listView.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = listView.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });
//--------------------------------

    }

    public synchronized void setParticipantList(ObservableList<String> participantList) {
        this.participantList = participantList;
        listView.setItems(participantList);
        listView.refresh();
    }

    @FXML
    void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isBlank()) {
            try {
                if (selectedRecipient != null) {
                    network.sendPrivateMessage(message, selectedRecipient);
                } else {
                    network.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка при отправке сообщения");
            }
            Platform.runLater(() -> appendMessage("Я", message));
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
        textArea.appendText(String.format("%1$td.%1$tm.%1$tY %1$tT", new Date()) + " @" + who + ": \n");
        textArea.appendText(message + "\n");
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

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
