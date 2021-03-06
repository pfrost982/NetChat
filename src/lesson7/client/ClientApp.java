package lesson7.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClientApp.class.getResource("Chat.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Net chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Network network = new Network();
        network.connect();
        ClientController clientController = loader.getController();
        clientController.setNetwork(network);
        network.waitMessage(clientController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
