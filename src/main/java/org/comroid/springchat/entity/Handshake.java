package org.comroid.springchat.entity;

import java.util.Collection;
import java.util.List;

public class Handshake {
    private final String username;
    private final Collection<OutputMessage> backlog;

    public String getUsername() {
        return username;
    }

    public Collection<OutputMessage> getBacklog() {
        return backlog;
    }

    public Handshake() {
        this(null, List.of());
    }

    public Handshake(String username, Collection<OutputMessage> backlog) {
        this.username = username;
        this.backlog = backlog;
    }
}
