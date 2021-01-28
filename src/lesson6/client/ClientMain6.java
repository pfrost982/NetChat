package lesson6.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain6 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClientMain6.class.getResource("clientChat.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Net chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Network6 network = new Network6();
        network.connect();
        ClientController6 clientController = loader.getController();
        clientController.setNetwork(network);
        network.waitMessage(clientController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
