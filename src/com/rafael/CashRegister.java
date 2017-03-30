package com.rafael;

import com.rafael.enumerator.DollarBillEnum;
import com.rafael.exception.CashRegisterException;
import com.rafael.helper.CommandHelper;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by Rafael on 3/28/17.
 */
public class CashRegister {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("MessagesBundle");

    //slots contain money bills. E.g.: $20, $10, $5, $2 and $1 use 5 slots
    private Integer [] billSlots;

    // this variable tells if the register is on/off
    private boolean on;

    public CashRegister() {

        //Initialize cash register
        billSlots = getNewBillSlots();

        //Turn cash register on
        on = true;

        // Ready to work
        print(BUNDLE.getString("cashregister.output.turnon"));

    }

    private Integer[] getNewBillSlots(){

        Integer[] billsChange = new Integer[DollarBillEnum.dollarBillEnumArray.length];
        for (int i = 0; i < billsChange.length; i++) {
            billsChange[i] = 0;
        }

        return billsChange;
    }

    private void print(String output) {
        System.out.println(output);
    }

    /**
     * This method executes the command line sent over.
     *
     * @param commandLine Command input string to be executed
     * @return if the cash register is still working (true) or was turned off (false)
     */
    public boolean executeCommand(String commandLine) {

        try {
            CommandHelper commandHelper = new CommandHelper(commandLine);

            String output = null;
            switch (commandHelper.getAction()) {
                case CommandHelper.COMMAND_PUT:
                    put(commandHelper.getArgs());

                case CommandHelper.COMMAND_SHOW:
                    output = show();
                    break;

                case CommandHelper.COMMAND_TAKE:
                    take(commandHelper.getArgs());
                    output = show();
                    break;

                case CommandHelper.COMMAND_CHANGE:
                    output = change(commandHelper.getArgs());
                    break;

                case CommandHelper.COMMAND_CHARGE:
                    output = charge(commandHelper.getArgs());
                    break;

                case CommandHelper.COMMAND_EXCHANGE:
                    output = exchange(commandHelper.getArgs());
                    break;

                case CommandHelper.COMMAND_QUIT:
                    output = quit();
                    break;

                default:
                    output = BUNDLE.getString("cashregister.error.invalidcommand");
            }

            print(output);

        } catch (CashRegisterException e) {
            print(e.getMessage());
        }

        return on;
    }

    private Integer getTotalCash() {
        return getTotalCash(billSlots);
    }

    private Integer getTotalCash(Integer[] array) {

        int total = 0;

        for (int i = 0; i < array.length; i++) {
            total += array[i] * DollarBillEnum.dollarBillEnumArray[i].getAmount();
        }

        return total;
    }

    private String getAmountBills() {
        return getAmountBills(billSlots);
    }

    private String getAmountBills(Integer[] array) {
        return String.format(BUNDLE.getString("cashregister.output.format.billamounts"), (Object[]) array);
    }

    private String show() {
        return String.format(BUNDLE.getString("cashregister.output.format.currentstate"), getTotalCash(), getAmountBills());
    }

    private void put(Integer[] args) {

        for (int i = 0; i < billSlots.length; i++) {
            billSlots[i] += args[i];
        }
    }

    private void take(Integer[] args) throws CashRegisterException {

        Integer[] billsTake = getNewBillSlots();

        for (int i = 0; i < billsTake.length; i++) {
            if (billSlots[i] < args[i]) {
                throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
            }
            billsTake[i] = args[i];
        }

        performWithdraw(billsTake);

    }

    private void performWithdraw(Integer[] args) throws CashRegisterException {
        for (int i = 0; i < billSlots.length; i++) {
            billSlots[i] -= args[i];
        }

    }

    private String change(Integer[] args) throws CashRegisterException {

        int amountToChange = args[0];
        Integer[] billsChange = getNewBillSlots();

        if (!recursiveFindChange(0, amountToChange, billsChange)){
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
        }

        performWithdraw(billsChange);

        return getAmountBills(billsChange);

    }
    private boolean recursiveFindChange(int indexDollarBill, int amountToChange, Integer[] billsChange) {
        return recursiveFindChange(indexDollarBill, amountToChange, billsChange, null);
    }

    private boolean recursiveFindChange(int indexDollarBill, int amountToChange, Integer[] billsChange, DollarBillEnum billToExchange) {

        boolean foundChange = false;
        int dollarBill = DollarBillEnum.dollarBillEnumArray[indexDollarBill].getAmount();
        int amountBills = Math.min(billSlots[indexDollarBill], amountToChange / dollarBill);

        // Ignore bill to be exchanged
        if (DollarBillEnum.dollarBillEnumArray[indexDollarBill] == billToExchange) {
            amountBills = 0;
        }

        amountToChange -= dollarBill * amountBills;

        foundChange = (amountToChange == 0);

        if (!foundChange && indexDollarBill < billSlots.length - 1) {

            while (!(foundChange = recursiveFindChange(indexDollarBill + 1, amountToChange, billsChange, billToExchange))) {

                // Return one bigger bill to break in smaller bills
                if (amountBills == 0) {
                    break;
                }

                amountBills--;
                amountToChange += dollarBill;
            }
        }

        billsChange[indexDollarBill] = amountBills;

        return foundChange;
    }

    private String charge(Integer[] args) throws CashRegisterException {

        int amountToCharge = args[0];
        Integer[] billsReceived = Arrays.copyOfRange(args, 1, args.length);
        int amountReceived = getTotalCash(billsReceived);
        int amountToChange = amountReceived - amountToCharge;

        if (amountReceived < amountToCharge) {
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.chargemissingmoney"));
        }

        Integer[] billsChange = getNewBillSlots();

        if (!recursiveFindChange(0, amountToChange, billsChange)){
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
        }

        put(billsReceived);

        performWithdraw(billsChange);

        return getAmountBills(billsChange);

    }

    private String exchange(Integer[] args) throws CashRegisterException {

        Integer[] billReceived = args;
        int amountReceived = getTotalCash(billReceived);

        Integer[] billsChange = getNewBillSlots();

        if (!recursiveFindChange(0, amountReceived, billsChange, DollarBillEnum.get(amountReceived))){
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
        }

        put(billReceived);

        performWithdraw(billsChange);

        return getAmountBills(billsChange);

    }

    private String quit() {
        on = false;

        return BUNDLE.getString("cashregister.output.turnoff");

    }

}
