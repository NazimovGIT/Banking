package ru.nazimov.BankAccounts.exception;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
