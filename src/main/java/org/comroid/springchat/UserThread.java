package org.comroid.springchat;

import java.io.*;
import java.net.Socket;

// newfile
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer; //

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();
            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New User connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = userName + "> " + clientMessage;
                server.broadcast(serverMessage, this);
            } while (!clientMessage.equals("exit")); // Todo: Button press? Wie?

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left the Chat";
            server.broadcast(serverMessage, this);
        } catch (IOException ex) {
            // TODO: Ausgabe eines Errors in UserThread
            ex.printStackTrace();
        }
    }

    void printUsers() {
        if (server.hasUsers()) {
            // TODO: Ausgabe aller Nutzer für den aktuellen Nutzer. Bisher nur Konsole.
            writer.println("Connected Users: " + server.getUserNames());
        } else {
            writer.println("No other Users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
