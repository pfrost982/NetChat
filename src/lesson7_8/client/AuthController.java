package lesson7_8.client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AuthController {

    private Network network;
    private ClientApp clientApp;
    private boolean notLog = true;

    @FXML
    private Label authLabel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    @FXML
    void initialize() {
        Thread thread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            try {
                int seconds = 0;
                while (seconds < 121) {
                    Thread.sleep(1000);
                    seconds = (int) ((System.currentTimeMillis() - startTime) / 1000);
                    String s = "Авторизуйтесь, у Вас осталось: " + (120 - seconds) + " секунд";
                    Platform.runLater(() -> authLabel.setText(s));
                }
                if (notLog) {
                    exit();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void exit() {
        System.exit(-3);
    }

    public void checkAuth() throws IOException {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.length() == 0 || password.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка ввода");
            alert.setContentText("Поля не должны быть пустыми");
            alert.show();
            return;
        }

        String authErrorMessage = network.sendAuthCommand(login, password);
        if (authErrorMessage == null) {
            notLog = false;
            clientApp.openChatWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка авторизации");
            alert.setContentText(authErrorMessage);
            alert.show();
            System.out.println("Ошибка аутентификации");
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }
}
