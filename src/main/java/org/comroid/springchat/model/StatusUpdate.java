package org.comroid.springchat.model;

public class StatusUpdate {
    public enum Type {
        USER_JOIN,
        USER_LEAVE
    }

    private Type type;
    private String detail;

    public StatusUpdate() {
        this(null, null);
    }

    public StatusUpdate(Type type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    public Type getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}
