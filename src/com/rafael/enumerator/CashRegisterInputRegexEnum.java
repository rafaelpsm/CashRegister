package com.rafael.enumerator;

/**
 * Created by Rafael on 3/29/17.
 */
public enum CashRegisterInputRegexEnum {
    COMMAND_5_ARGS  ("^[a-z]{3,6}( [0-9]+){5}$"),
    COMMAND_1_ARG   ("^[a-z]{3,6}( [0-9]+){1}$"),
    COMMAND_NO_ARGS ("^[a-z]{3,6}$"),
    COMMAND_GENERIC ("^[a-z]{3,6}( [0-9]+)*$");

    private String regex;

    CashRegisterInputRegexEnum(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
