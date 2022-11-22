package org.comroid.springchat.model;

public class StatusUpdate implements BacklogMessage {
    private final Type type;
    private final String detail;

    public Type getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public BacklogMessage.Type getBacklogType() {
        return BacklogMessage.Type.StatusUpdate;
    }

    public StatusUpdate() {
        this(null, null);
    }

    public StatusUpdate(Type type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    public enum Type {
        USER_JOIN,
        USER_LEAVE
    }
}
