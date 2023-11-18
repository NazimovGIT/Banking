package ru.nazimov.BankAccounts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nazimov.BankAccounts.dto.AccountErrorResponse;
import ru.nazimov.BankAccounts.exception.AccountException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.nazimov.BankAccounts.util.ErrorUtil.getHttpStatus;

@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<AccountErrorResponse> handleAccountException(AccountException e) {
        AccountErrorResponse response = new AccountErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, getHttpStatus(e));
    }

    @ExceptionHandler
    private ResponseEntity<AccountErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        AccountErrorResponse response = new AccountErrorResponse("Ошибка валидации.");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        response.setValidationErrors(errors.stream()
                .collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage, (a, b) -> a + " " + b)));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<AccountErrorResponse> handleException(Exception e) {
        AccountErrorResponse response = new AccountErrorResponse("Что-то пошло не так.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
