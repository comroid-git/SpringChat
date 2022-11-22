package org.comroid.springchat.controller;

import org.comroid.springchat.entity.Handshake;
import org.comroid.springchat.entity.Message;
import org.comroid.springchat.entity.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {
    public static final String CMD_COLOR = "/color ";
    private final Map<String, String> Colors = new ConcurrentHashMap<>();
    private final Set<String> Usernames = new HashSet<>();
    private final List<OutputMessage> backlog = new ArrayList<>();
    private final static int MAX_BACKLOG = 50;

    @MessageMapping("/msg")
    @SendTo("/topic/messages")
    public OutputMessage handleMessage(Message msg) {
        String from = msg.getFrom();
        String text = msg.getText();
        if (text.startsWith(CMD_COLOR)) {
            String color = text.substring(CMD_COLOR.length());
            Colors.put(from, color);
        }
        String color = Colors.getOrDefault(from, "green");
        if (text.startsWith("/"))
            return null;
        return appendToBacklog(new OutputMessage(from, text, color));
    }

    @MessageMapping("/users/handshake")
    @SendTo("/topic/handshake")
    public Handshake userHandshake(String username) {
        return new Handshake(username, backlog);
    }

    @MessageMapping("/users/join")
    @SendTo("/topic/users")
    public Set<String> userJoin(String username) {
        Usernames.add(username);
        return Usernames;
    }

    @MessageMapping("/users/leave")
    @SendTo("/topic/users")
    public Set<String> userLeave(String username) {
        Usernames.remove(username);
        return Usernames;
    }

    private OutputMessage appendToBacklog(OutputMessage msg) {
        if (backlog.size() >= MAX_BACKLOG)
            backlog.remove(0);
        backlog.add(msg);
        return msg;
    }
}
