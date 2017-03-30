package com.rafael.enumerator;

/**
 * Created by Rafael on 3/29/17.
 */
public enum CashRegisterCommandEnum {
    CHANGE  ("change"),
    CHARGE  ("charge"),
    PUT     ("put"),
    SHOW    ("show"),
    TAKE    ("take"),
    QUIT    ("quit");

    private String command;

    CashRegisterCommandEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
