package ru.nazimov.BankAccounts.exception;

public class AccountAuthorizationException extends AccountException {
    public AccountAuthorizationException(String msg) {
        super(msg);
    }
}
