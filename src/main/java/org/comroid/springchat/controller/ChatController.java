package org.comroid.springchat.controller;

import org.comroid.springchat.entity.Message;
import org.comroid.springchat.entity.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {
    public static final String CMD_COLOR = "/color ";
    private final Map<String, String> Colors = new ConcurrentHashMap<>();

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
        return new OutputMessage(from, text, color);
    }
}
