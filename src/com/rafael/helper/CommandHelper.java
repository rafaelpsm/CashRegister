package com.rafael.helper;

import com.rafael.enumerator.ActionEnum;
import com.rafael.enumerator.CashRegisterInputRegexEnum;
import com.rafael.exception.CashRegisterException;

import java.util.ResourceBundle;

/**
 * Created by Rafael on 3/29/17.
 */
public class CommandHelper {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("MessagesBundle");
    private static final String DELIMITER_SPACE = " ";

    private ActionEnum action;
    private Integer[] args;

    protected String commandLine;

    public CommandHelper(String commandLine) throws CashRegisterException {

        this.commandLine = commandLine;

        // Break arguments
        String[] args = commandLine.split(DELIMITER_SPACE);

        this.action = ActionEnum.get(args[0]);

        //Validate command line
        if (!this.isCommandLineValid()) {
            return;
        }

        Integer[] commandArgs = new Integer[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            commandArgs[i - 1] = new Integer(args[i]);
        }
        this.args = commandArgs;

    }

    /**
     * This method executes a generic validation on the command line and
     * a specific validation on the arguments of the command line
     *
     * @return if the command line on the instance has a valid format
     */
    private boolean isCommandLineValid() throws CashRegisterException {

        // Validate commandLine format in general
        if (!this.isCommandLineValid(CashRegisterInputRegexEnum.COMMAND_GENERIC)) {

            throw new CashRegisterException(BUNDLE.getString("cashregister.error.invalidcommand"));

        }

        CashRegisterInputRegexEnum argsRegex = null;

        switch (this.action) {
            case COMMAND_CHARGE:
                argsRegex = CashRegisterInputRegexEnum.COMMAND_6_ARGS;
                break;

            case COMMAND_EXCHANGE:
                argsRegex = CashRegisterInputRegexEnum.COMMAND_EXCHANGE;
                break;

            case COMMAND_PUT:
            case COMMAND_TAKE:
                argsRegex = CashRegisterInputRegexEnum.COMMAND_5_ARGS;
                break;

            case COMMAND_CHANGE:
                argsRegex = CashRegisterInputRegexEnum.COMMAND_1_ARG;
                break;

            case COMMAND_SHOW:
            case COMMAND_QUIT:
                argsRegex = CashRegisterInputRegexEnum.COMMAND_NO_ARGS;
                break;

            default:
                throw new CashRegisterException(BUNDLE.getString("cashregister.error.invalidcommand"));
        }

        //Validate arguments for specific command
        if (!this.isCommandLineValid(argsRegex)) {
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.invalidcommand"));
        }

        return true;

    }

    /**
     * This method matches the command line against the regex sent on the parameter.
     *
     * @param pattern Pattern to be matched against the command line
     * @return if the command line matches the pattern
     */
    private boolean isCommandLineValid(CashRegisterInputRegexEnum pattern) {

        return this.commandLine.matches(pattern.getRegex());

    }

    public ActionEnum getAction() {
        return action;
    }

    public Integer[] getArgs() {
        return args;
    }

}
