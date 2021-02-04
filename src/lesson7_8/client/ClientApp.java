package lesson7_8.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    private Network network;
    private Stage primaryStage;
    private Stage authStage;
    private ChatController chatController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        network = new Network();
        network.connect();

        openAuthWindow();
    }

    private void openAuthWindow() throws IOException {
        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(ClientApp.class.getResource("auth.fxml"));

        Parent root = authLoader.load();
        authStage = new Stage();

        authStage.setTitle("Authentication");
        authStage.setScene(new Scene(root));
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        authStage.show();

        AuthController authController = authLoader.getController();
        authController.setNetwork(network);
        authController.setClientApp(this);
    }

    public void openChatWindow() throws IOException {
        FXMLLoader chatLoader = new FXMLLoader();
        chatLoader.setLocation(ClientApp.class.getResource("chat.fxml"));
        Parent root = chatLoader.load();
        primaryStage.setScene(new Scene(root));

        chatController = chatLoader.getController();
        chatController.setNetwork(network);

        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        //primaryStage.setAlwaysOnTop(true);
        network.waitMessage(chatController);
        //chatController.setUsernameTitle(network.getUsername());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
