package lesson7_8.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class AuthController {

    private Network network;
    private ClientApp clientApp;

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;


    public void checkAuth() throws IOException {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if(login.length() == 0 || password.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setHeaderText("Ошибка ввода");
            alert.setContentText("Поля не должны быть пустыми");
            alert.show();
            return;
        }

        String authErrorMessage = network.sendAuthCommand(login, password);
        if (authErrorMessage == null) {
            clientApp.openChatWindow();
        }
        else {
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
