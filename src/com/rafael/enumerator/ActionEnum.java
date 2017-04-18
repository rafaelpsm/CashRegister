package com.rafael.enumerator;

import com.rafael.exception.CashRegisterException;

import java.util.ResourceBundle;

/**
 * Created by Rafael on 4/11/17.
 */
public enum ActionEnum {

    COMMAND_CHANGE   ("change"),
    COMMAND_CHARGE   ("charge"),
    COMMAND_EXCHANGE ("exchange"),
    COMMAND_PUT      ("put"),
    COMMAND_SHOW     ("show"),
    COMMAND_TAKE     ("take"),
    COMMAND_QUIT     ("quit");

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("MessagesBundle");
    private final String name;

    public static ActionEnum get(String action) throws CashRegisterException {

        for (ActionEnum a: values()) {
            if (a.getName().equals(action)) {
                return a;
            }
        }

        throw new CashRegisterException(BUNDLE.getString("cashregister.error.invalidcommand"));
    }

    ActionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
