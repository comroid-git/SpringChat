package org.comroid.springchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// newfile
public class ChatServer {
    private int port;
    private List<String> userNames = new ArrayList<>();
    private List<UserThread> userThreads = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void addUserName(String name) {
        // TODO: Username basteln
        userNames.add(name);
    }

    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        // TODO: Ausgabe, dass der User den Chat verlassen hat.
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public Collection<String> getUserNames() {
        return userNames;
    }
}
