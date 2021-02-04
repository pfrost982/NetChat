package lesson7_8.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + pass
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg"; // + msg
    private static final String SERVER_MSG_CMD_PREFIX = "/serverMsg"; // + msg
    private static final String PRIVATE_MSG_CMD_PREFIX = "/w"; //sender + p + msg
    private static final String END_CMD_PREFIX = "/end"; //
    private static final String CLIENTS_LIST_PREFIX = "/clients";

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

    public synchronized void broadcastClientMsg(String msg, ClientHandler sender) {
        for (ClientHandler o : clients) {
            if (o == sender) {
                continue;
            }
            o.sendMsg(CLIENT_MSG_CMD_PREFIX + " " + sender.getName() + " " + msg);
        }
    }

    public synchronized void broadcastServerMsg(String msg) {
        for (ClientHandler o : clients) {
            String command = String.format("%s %s", SERVER_MSG_CMD_PREFIX, msg);
            //o.sendMsg(SERVER_MSG_CMD_PREFIX + " " + msg);
            o.sendMsg(command);
        }
    }

    public synchronized void personalMsg(String destName, String msg, ClientHandler sender) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(destName)) {
                String command = String.format("%s %s %s", PRIVATE_MSG_CMD_PREFIX, sender.getName(), msg);
                //o.sendMsg(PRIVATE_MSG_CMD_PREFIX + " " + sender.getName() + " " + msg);
                o.sendMsg(command);
                return;
            }
        }
        String command = String.format("%s %s не существует", SERVER_MSG_CMD_PREFIX, destName);
        //sender.sendMsg(SERVER_MSG_CMD_PREFIX + " " + destName + " не существует");
        sender.sendMsg(command);
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
        broadcastClientsList();
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
        broadcastClientsList();
    }

    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder(CLIENTS_LIST_PREFIX + " ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        for (ClientHandler o : clients) {
            o.sendMsg(sb.toString());
        }
    }
}
