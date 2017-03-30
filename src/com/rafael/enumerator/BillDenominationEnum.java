package com.rafael.enumerator;

/**
 * Created by Rafael on 3/29/17.
 */
public enum BillDenominationEnum {

    BILL_20 (20),
    BILL_10 (10),
    BILL_5  (5),
    BILL_2  (2),
    BILL_1  (1);

    private final int amount;

    //Singleton array of dollar bill types
    public static BillDenominationEnum[] billDenominationEnumArray = new BillDenominationEnum[]{
            BILL_20,
            BILL_10,
            BILL_5,
            BILL_2,
            BILL_1
    };

    public static BillDenominationEnum get(int amount) {

        if (BILL_20.getAmount() == amount) {
            return BILL_20;
        } else if (BILL_10.getAmount() == amount) {
            return BILL_10;
        } else if (BILL_5.getAmount() == amount) {
            return BILL_5;
        } else if (BILL_2.getAmount() == amount) {
            return BILL_2;
        } else if (BILL_1.getAmount() == amount) {
            return BILL_1;
        }

        return null;
    }

    BillDenominationEnum(int factor) {
        amount = factor;
    }

    public int getAmount() {
        return amount;
    }

}
