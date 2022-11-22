package org.comroid.springchat.controller;

import org.comroid.springchat.model.Handshake;
import org.comroid.springchat.model.Message;
import org.comroid.springchat.model.OutputMessage;
import org.comroid.springchat.model.StatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {
    public static final String CMD_COLOR = "/color ";
    private final Map<String, String> Colors = new ConcurrentHashMap<>();
    private final Set<String> Users = new HashSet<>();
    private final List<OutputMessage> backlog = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate broadcast;
    private final static int MAX_BACKLOG = 50;

    @MessageMapping("/msg")
    @SendTo("/topic/messages")
    public OutputMessage handleMessage(Message msg) {
        if (!Users.contains(msg.getFrom()))
            // ignore messages from non-handshaked users (security reasons)
            return null;
        String from = msg.getFrom();
        String text = msg.getText();
        if (text.startsWith(CMD_COLOR)) {
            String color = text.substring(CMD_COLOR.length());
            Colors.put(from, color);
        }
        String color = Colors.getOrDefault(from, "white");
        if (text.startsWith("/"))
            return null;
        return appendToBacklog(new OutputMessage(from, text, color));
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
        broadcast.convertAndSend("/topic/status", new StatusUpdate(StatusUpdate.Type.USER_JOIN, username));
        return Users;
    }

    @MessageMapping("/users/leave")
    @SendTo("/topic/users")
    public Set<String> userLeave(String username) {
        broadcast.convertAndSend("/topic/status", new StatusUpdate(StatusUpdate.Type.USER_LEAVE, username));
        Users.remove(username);
        return Users;
    }

    private OutputMessage appendToBacklog(OutputMessage msg) {
        if (backlog.size() >= MAX_BACKLOG)
            backlog.remove(0);
        backlog.add(msg);
        return msg;
    }
}
