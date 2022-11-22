package org.comroid.springchat.model;

public interface BacklogMessage {
    Type getBacklogType();

    enum Type {
        Message,
        StatusUpdate
    }
}
