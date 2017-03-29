package com.rafael.enumerator;

/**
 * Created by Rafael on 3/29/17.
 */
public enum DollarBillEnum {

    BILL_20 (20),
    BILL_10 (10),
    BILL_5  (5),
    BILL_2  (2),
    BILL_1  (1);

    private final int dollarBillFactor;

    //Singleton array of dollar bill types
    public static DollarBillEnum[] dollarBillEnumArray = new DollarBillEnum[]{
            BILL_20,
            BILL_10,
            BILL_5,
            BILL_2,
            BILL_1
    };

    DollarBillEnum (int factor) {
        dollarBillFactor = factor;
    }

    public int getDollarBillFactor() {
        return dollarBillFactor;
    }

}
