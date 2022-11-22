package org.comroid.springchat.model;

import java.util.Calendar;

public class OutputMessage extends Message {
    private final String time;
    private final String color;

    public String getTime() {
        return time;
    }

    public String getColor() {
        return color;
    }

    public OutputMessage(String from, String text, String color) {
        super(from, text);
        var cld = Calendar.getInstance();
        this.time = String.format("%02d:%02d:%02d", cld.get(Calendar.HOUR_OF_DAY), cld.get(Calendar.MINUTE), cld.get(Calendar.SECOND));
        this.color = color;
    }
}
