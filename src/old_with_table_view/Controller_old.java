package old_with_table_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller_old {
    @FXML
    private ListView<String> listView;

    @FXML
    private TableView<RowWord> tableView;

    @FXML
    private TableColumn<RowWord, String> wordTableView;

    @FXML
    private TableColumn<RowWord, Integer> countTableView;

    private final ObservableList<String> wordList = FXCollections.observableArrayList(
            "Новый год", "Праздник", "Подарки", "Елка", "Украшения");

    private final ObservableList<RowWord> frequencyByWord = FXCollections.observableArrayList(
            new RowWord("a", 1),
            new RowWord("b", 2),
            new RowWord("c", 3)
    );

    @FXML
    private TextField inputField;

    @FXML
    void initialize() {
        listView.setItems(wordList);
        tableView.setItems(frequencyByWord);
        wordTableView.setCellValueFactory(new PropertyValueFactory<>("word"));
        countTableView.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String message = inputField.getText().trim();
        if (!message.isBlank()) {
            addMessageToList(message);
            addMessageToTable(message);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка ввода сообщения");
            alert.setContentText("Нельзя отправлять пустое сообщение");
            alert.show();
        }
        inputField.clear();
    }

    private void addMessageToList(String message) {
        listView.getItems().add(message);
    }

    private void addMessageToTable(String message) {
        for (RowWord rowWord : frequencyByWord) {
            if (message.equals(rowWord.getWord())) {
                rowWord.incCount();
                //int frequency = rowWord.getCount() + 1;
                //frequencyByWord.remove(rowWord);
                //frequencyByWord.add(new RowWord(message, frequency));
                tableView.refresh();
                return;
            }
        }
        frequencyByWord.add(new RowWord(message, 1));
    }

    @FXML
    void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Сетевой чат");
        alert.setContentText("В будующем из этого получится сетевой чат, надеюсь...)))");
        alert.show();
    }

    @FXML
    void delete(ActionEvent event) {
        if (listView.getItems().size() > 0) {
            listView.getItems().remove(listView.getItems().size() - 1);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete error");
            alert.setHeaderText("Ошибка удаления сообщения");
            alert.setContentText("Нельзя удалять когда список сообщений пуст");
            alert.show();
        }
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }
}
