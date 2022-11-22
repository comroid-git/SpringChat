package org.comroid.springchat.model;

import java.util.Collection;
import java.util.List;

public class Handshake {
    private final String username;
    private final Collection<BacklogMessage> backlog;

    public String getUsername() {
        return username;
    }

    public Collection<BacklogMessage> getBacklog() {
        return backlog;
    }

    public Handshake() {
        this(null, List.of());
    }

    public Handshake(String username, Collection<BacklogMessage> backlog) {
        this.username = username;
        this.backlog = backlog;
    }
}
