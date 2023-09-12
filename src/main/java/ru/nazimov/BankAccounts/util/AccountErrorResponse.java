package ru.nazimov.BankAccounts.util;

public class AccountErrorResponse {
    private String message;
    private long timestamp;

    public AccountErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
