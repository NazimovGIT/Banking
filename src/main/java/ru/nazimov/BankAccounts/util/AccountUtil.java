package ru.nazimov.BankAccounts.util;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AccountUtil {
    private static final Random random = new Random();

    public static String getAccountNumber() {
        StringBuilder number = new StringBuilder();
        for (int n = 0; n < 20; n++) {
            number.append(random.nextInt(0, 10));
        }
        return number.toString();
    }

}
