package ru.nazimov.BankAccounts.util;

public class AccountUtil {
    public static String getAccountNumber() {
        StringBuilder number = new StringBuilder();
        for (int n = 0; n < 20; n++) {
            number.append((int) (Math.random() * 10));
        }
        return number.toString();
    }
}
