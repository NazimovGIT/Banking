package ru.nazimov.BankAccounts.exception;

public class AccountNotCreatedException extends AccountException {
    public AccountNotCreatedException(String msg) {
        super(msg);
    }
}
