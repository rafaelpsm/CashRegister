package com.rafael.enumerator;

/**
 * Created by Rafael on 3/29/17.
 */
public enum CashRegisterInputRegexEnum {
    COMMAND_6_ARGS  ("^[a-z]{3,8}( [0-9]+){6}$"),
    COMMAND_5_ARGS  ("^[a-z]{3,8}( [0-9]+){5}$"),
    COMMAND_1_ARG   ("^[a-z]{3,8}( [0-9]+){1}$"),
    COMMAND_NO_ARGS ("^[a-z]{3,8}$"),
    COMMAND_GENERIC ("^[a-z]{3,8}( [0-9]+)*$"),
    COMMAND_EXCHANGE("^[a-z]{3,8} (1 0 0 0|0 1 0 0|0 0 1 0|0 0 0 1)* 0$");

    private String regex;

    CashRegisterInputRegexEnum(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
