package lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerMain {
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Ожидание подключения...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Соединение установлено");

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            Thread thread = new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in)) {
                    String message;
                    while (true) {
                        System.out.print("Сервер говорит: ");
                        message = scanner.nextLine();
                        out.writeUTF(message);
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка сканера сервера");
                }
            });
            thread.setDaemon(true);
            thread.start();

            try {
                while (true) {
                    String message = in.readUTF();
                    System.out.println("\rПользователь говорит" + ": " + message);
                    System.out.print("Сервер говорит: ");
                }
            } catch (IOException e) {
                clientSocket.close();
                System.out.println("Клиент отключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
