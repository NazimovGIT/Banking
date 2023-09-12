package ru.nazimov.BankAccounts.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nazimov.BankAccounts.util.AccountErrorResponse;
import ru.nazimov.BankAccounts.util.Exceptions.AccountException;

import static ru.nazimov.BankAccounts.util.ErrorUtil.getErrorMessage;
import static ru.nazimov.BankAccounts.util.ErrorUtil.getHttpStatus;

@RestControllerAdvice
public class AccountsExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<AccountErrorResponse> handleAccountException(AccountException e) {
        AccountErrorResponse response = new AccountErrorResponse
                (e.getMessage(),
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, getHttpStatus(e));
    }

    @ExceptionHandler
    private ResponseEntity<AccountErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        AccountErrorResponse response = new AccountErrorResponse
                (getErrorMessage(e),
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
