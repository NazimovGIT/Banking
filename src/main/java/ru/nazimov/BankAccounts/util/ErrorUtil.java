package ru.nazimov.BankAccounts.util;

import org.springframework.http.HttpStatus;
import ru.nazimov.BankAccounts.exception.AccountAuthorizationException;
import ru.nazimov.BankAccounts.exception.AccountException;
import ru.nazimov.BankAccounts.exception.AccountNotFoundException;

public class ErrorUtil {

    public static HttpStatus getHttpStatus(AccountException e) {
        return e instanceof AccountNotFoundException ? HttpStatus.NOT_FOUND :
                e instanceof AccountAuthorizationException ? HttpStatus.UNAUTHORIZED :
                        HttpStatus.BAD_REQUEST;
    }

}
