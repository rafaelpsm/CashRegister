package com.rafael;

import com.rafael.enumerator.BillDenominationEnum;
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
    private Integer [] billDenominations;

    // this variable tells if the register is on/off
    private boolean on;

    public CashRegister() {

        //Initialize cash register
        billDenominations = getNewBillSlots();

        //Turn cash register on
        on = true;

        // Ready to work
        print(BUNDLE.getString("cashregister.output.turnon"));

    }

    private Integer[] getNewBillSlots(){

        Integer[] billsChange = new Integer[BillDenominationEnum.billDenominationEnumArray.length];
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
                case COMMAND_PUT:
                    put(commandHelper.getArgs());

                case COMMAND_SHOW:
                    output = show();
                    break;

                case COMMAND_TAKE:
                    take(commandHelper.getArgs());
                    output = show();
                    break;

                case COMMAND_CHANGE:
                    output = change(commandHelper.getArgs());
                    break;

                case COMMAND_CHARGE:
                    output = charge(commandHelper.getArgs());
                    break;

                case COMMAND_EXCHANGE:
                    output = exchange(commandHelper.getArgs());
                    break;

                case COMMAND_QUIT:
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

    // ###########################################################################################################

    /**
     *
     * @return The current state of the Cash Register in total and bill denominations
     */
    private String show() {
        return String.format(BUNDLE.getString("cashregister.output.format.currentstate"), getTotalCash(), getCurrentStateBills());
    }

    /**
     *
     * @return The total amount in cash
     */
    private Integer getTotalCash() {
        return getTotalCash(billDenominations);
    }

    /**
     *
     * @return The total amount in cash
     */
    private Integer getTotalCash(Integer[] array) {

        int total = 0;

        for (int i = 0; i < array.length; i++) {
            total += array[i] * BillDenominationEnum.billDenominationEnumArray[i].getAmount();
        }

        return total;
    }

    /**
     *
     * @return A string containing the current state on bill denominations
     */
    private String getCurrentStateBills() {
        return getCurrentStateBills(billDenominations);
    }

    /**
     *
     * @return A string containing the current state on bill denominations
     */
    private String getCurrentStateBills(Integer[] array) {
        return String.format(BUNDLE.getString("cashregister.output.format.billamounts"), (Object[]) array);
    }

    // ###########################################################################################################

    /**
     * This method adds bill denomination into the Cash Register
     *
     * @param args Bill denominations to be added
     */
    private void put(Integer[] args) {

        for (int i = 0; i < billDenominations.length; i++) {
            billDenominations[i] += args[i];
        }
    }

    // ###########################################################################################################

    /**
     * This method take bill denominations from the Cash Register
     *
     * @param args Bill denominations to be taken
     * @throws CashRegisterException if there is not enough bills
     */
    private void take(Integer[] args) throws CashRegisterException {

        Integer[] billsTake = getNewBillSlots();

        for (int i = 0; i < billsTake.length; i++) {
            if (billDenominations[i] < args[i]) {
                throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
            }
            billsTake[i] = args[i];
        }

        performWithdraw(billsTake);

    }

    /**
     * This method perform a withdraw of bill denominations from the Cash Register
     *
     * @param args Bill denominations to be taken
     */
    private void performWithdraw(Integer[] args) {
        for (int i = 0; i < billDenominations.length; i++) {
            billDenominations[i] -= args[i];
        }

    }

    // ###########################################################################################################

    /**
     * This method calculates the change to be return to the customer.
     *
     * @param args the amount to be changed
     * @return the bill denominations chenged
     * @throws CashRegisterException if there are not enough bills
     */
    private String change(Integer[] args) throws CashRegisterException {

        int amountToChange = args[0];
        Integer[] billsChange = getNewBillSlots();

        if (!recursiveFindChange(0, amountToChange, billsChange)){
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
        }

        performWithdraw(billsChange);

        return getCurrentStateBills(billsChange);

    }

    private boolean recursiveFindChange(int indexDollarBill, int amountToChange, Integer[] billsChange) {
        return recursiveFindChange(indexDollarBill, amountToChange, billsChange, null);
    }

    private boolean recursiveFindChange(int indexDollarBill, int amountToChange, Integer[] billsChange, BillDenominationEnum billToExchange) {

        boolean foundChange = false;
        int dollarBill = BillDenominationEnum.billDenominationEnumArray[indexDollarBill].getAmount();
        int amountBills = Math.min(billDenominations[indexDollarBill], amountToChange / dollarBill);

        // Ignore bill to be exchanged
        if (BillDenominationEnum.billDenominationEnumArray[indexDollarBill] == billToExchange) {
            amountBills = 0;
        }

        amountToChange -= dollarBill * amountBills;

        foundChange = (amountToChange == 0);

        if (!foundChange && indexDollarBill < billDenominations.length - 1) {

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

    // ###########################################################################################################

    /**
     * This method charge the customer a certain amount based on the receiving amount of money
     * and give back the change.
     *
     * @param args The amount to charge and the bill denominations received
     * @return the change
     * @throws CashRegisterException if there are not enough bills for the change
     */
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

        return getCurrentStateBills(billsChange);

    }

    // ###########################################################################################################

    /**
     * This method perform an exchange of money, breaking a bill denominations into smaller value bills.
     *
     * @param args The amount to charge and the bill denominations received
     * @return the change
     * @throws CashRegisterException if there are not enough bills for the change
     */
    private String exchange(Integer[] args) throws CashRegisterException {

        Integer[] billReceived = args;
        int amountReceived = getTotalCash(billReceived);

        Integer[] billsChange = getNewBillSlots();

        if (!recursiveFindChange(0, amountReceived, billsChange, BillDenominationEnum.get(amountReceived))){
            throw new CashRegisterException(BUNDLE.getString("cashregister.error.billnotavailable"));
        }

        put(billReceived);

        performWithdraw(billsChange);

        return getCurrentStateBills(billsChange);

    }

    // ###########################################################################################################

    /**
     * This method should turn off the Cash Register
     *
     * @return whether the Cash Register is on or off
     */
    private String quit() {
        on = false;

        return BUNDLE.getString("cashregister.output.turnoff");

    }

}
