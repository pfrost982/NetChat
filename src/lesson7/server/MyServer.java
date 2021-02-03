package lesson7.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class MyServer {
    private final int PORT = 8888;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMsg(String msg, ClientHandler sender) {
        for (ClientHandler o : clients) {
            if (o == sender){
                continue;
            }
            o.sendMsg(sender.getName() + ": " + msg);
        }
    }

    public synchronized void personalMsg(String destName, String msg, ClientHandler sender) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(destName)) {
                o.sendMsg("Приватно от " + sender.getName() + ": " + msg);
                return;
            }
            sender.sendMsg("Имя: " + destName + " не существует");
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
    }
}
