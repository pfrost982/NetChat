package lesson7_8.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + pass
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg"; // + msg
    private static final String SERVER_MSG_CMD_PREFIX = "/serverMsg"; // + msg
    private static final String PRIVATE_MSG_CMD_PREFIX = "/w"; //sender + p + msg
    private static final String END_CMD_PREFIX = "/end"; //
    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "Unregistered";
            new Thread(() -> {
                try {
                    authentication();
                    waitMessage();
                } catch (IOException e) {
                    System.out.println(this.name + " отключился");
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String strFromChat = in.readUTF();
            String[] parts = strFromChat.split("\\s+", 3);
            String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
            if (nick != null) {
                if (!myServer.isNickBusy(nick)) {
                    sendMsg(AUTHOK_CMD_PREFIX + " " + nick);
                    name = nick;
                    myServer.broadcastServerMsg(this.name + " авторизовался");
                    myServer.subscribe(this);
                    System.out.println(name + " авторизовался");
                    return;
                } else {
                    sendMsg(AUTHERR_CMD_PREFIX + " " + "Учетная запись уже используется");
                }
            } else {
                sendMsg(AUTHERR_CMD_PREFIX + " " + "Неверные логин/пароль");
            }
        }
    }

    public void waitMessage() throws IOException {
        while (true) {
            String strFromChat = in.readUTF();
            System.out.println("от " + name + ": " + strFromChat);
            if (strFromChat.startsWith(END_CMD_PREFIX)) {
                return;
            } else if (strFromChat.startsWith(PRIVATE_MSG_CMD_PREFIX)) {
                String[] parts = strFromChat.split("\\s+", 3);
                myServer.personalMsg(parts[1], parts[2], this);
            } else myServer.broadcastClientMsg(strFromChat, this);
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        System.out.println(this.name + " соединение закрыто");
        myServer.unsubscribe(this);
        myServer.broadcastServerMsg(this.name + " вышел из чата");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
