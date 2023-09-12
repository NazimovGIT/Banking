package ru.nazimov.BankAccounts.util;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.nazimov.BankAccounts.util.Exceptions.AccountAuthorizationException;
import ru.nazimov.BankAccounts.util.Exceptions.AccountException;
import ru.nazimov.BankAccounts.util.Exceptions.AccountNotFoundException;

import java.util.List;

public class ErrorUtil {
    public static String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append("; ");
        }
        return errorMsg.delete(errorMsg.length() - 2, errorMsg.length()).toString();
    }

    public static HttpStatus getHttpStatus(AccountException e) {
        return e instanceof AccountNotFoundException ? HttpStatus.NOT_FOUND :
                e instanceof AccountAuthorizationException ? HttpStatus.UNAUTHORIZED :
                        HttpStatus.BAD_REQUEST;
    }
}
