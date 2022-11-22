package org.comroid.springchat.model;

public class Message {
    private String from;
    private String text;

    public Message() {
        this(null, null);
    }

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }
}
