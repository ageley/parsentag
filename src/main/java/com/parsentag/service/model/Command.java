package com.parsentag.service.model;

public enum Command {
    START("/start"),
    TAGS("/tags"),
    CLEAR("/clear");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Command fromValue(String value) {
        if (value == null) {
            return null;
        }

        return switch (value) {
            case "/start" -> START;
            case "/tags" -> TAGS;
            case "/clear" -> CLEAR;
            default -> null;
        };
    }
}
