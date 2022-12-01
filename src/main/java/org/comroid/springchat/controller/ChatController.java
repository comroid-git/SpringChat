package org.comroid.springchat.controller;

import org.comroid.cmdr.CommandManager;
import org.comroid.springchat.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {
    private final static int MAX_BACKLOG = 50;
    private final Map<String, String> Colors = new ConcurrentHashMap<>();
    private final Set<String> Users = new HashSet<>();
    private final Map<String, String> Nicks = new ConcurrentHashMap<>();

    private final List<BacklogMessage> backlog = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate broadcast;
    @Autowired
    private CommandManager cmdr;

    @MessageMapping("/msg")
    @SendTo("/topic/messages")
    public OutputMessage handleMessage(Message msg) {
        if (!Users.contains(msg.getFrom()))
            // ignore messages from non-handshaked users (security reasons)
            return null;
        String from = msg.getFrom();
        String text = msg.getText();
        if (text.startsWith("/"))
        {
            cmdr.executeCommand(cmdr, text.substring(1).split(" "), new Object[]{from});
            return null;
        }
        String color = Colors.getOrDefault(from, "white");
        from = Nicks.getOrDefault(from, from);
        OutputMessage output = new OutputMessage(from, text, color);
        appendToBacklog(output);
        return output;
    }

    @MessageMapping("/users/handshake")
    @SendTo("/topic/handshake")
    public Handshake userHandshake(String username) {
        int c = 1;
        while (Users.contains(username))
            if (c == 1)
                username += c++;
            else username = username.substring(0, username.length() - String.valueOf(c).length()) + c++;
        Users.add(username);
        return new Handshake(username, backlog);
    }

    @MessageMapping("/users/join")
    @SendTo("/topic/users")
    public Set<String> userJoin(String username) {
        StatusUpdate update = new StatusUpdate(StatusUpdate.Type.USER_JOIN, username);
        appendToBacklog(update);
        broadcast.convertAndSend("/topic/status", update);
        return Users;
    }

    @MessageMapping("/users/leave")
    @SendTo("/topic/users")
    public Set<String> userLeave(String username) {
        StatusUpdate update = new StatusUpdate(StatusUpdate.Type.USER_LEAVE, username);
        appendToBacklog(update);
        broadcast.convertAndSend("/topic/status", update);
        Users.remove(username);
        return Users;
    }

    private void appendToBacklog(BacklogMessage msg) {
        if (backlog.size() >= MAX_BACKLOG)
            backlog.remove(0);
        backlog.add(msg);
    }
}
