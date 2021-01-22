package lesson6.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private static final int DEFAULT_SERVER_SOCKET = 8888;
    private static final String DEFAULT_SERVER_HOST = "localhost";

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final int port;
    private final String host;

    public Network(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public Network() {
        this.host = DEFAULT_SERVER_HOST;
        this.port = DEFAULT_SERVER_SOCKET;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Соединение не установлено");
            e.printStackTrace();
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void waitMessage(ClientController Controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();
                    Controller.appendMessage("Сервер", message);
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения");
            }

        });
        thread.setDaemon(true);
        thread.start();
    }
}
