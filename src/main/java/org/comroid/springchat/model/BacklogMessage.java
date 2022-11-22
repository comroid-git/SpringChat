package org.comroid.springchat.model;

public abstract class BacklogMessage {
    public abstract Type getBacklogType();

    public enum Type {
        Message,
        StatusUpdate
    }
}
