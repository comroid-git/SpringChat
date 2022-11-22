package org.comroid.springchat.model;

public class Message extends BacklogMessage {
    private final String from;
    private final String text;

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    @Override
    public Type getBacklogType() {
        return Type.Message;
    }

    public Message() {
        this(null, null);
    }

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }
}
