package ru.nazimov.BankAccounts.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.nazimov.BankAccounts.dto.*;
import ru.nazimov.BankAccounts.services.AccountsService;
import ru.nazimov.BankAccounts.util.*;
import ru.nazimov.BankAccounts.util.Exceptions.AccountException;


import java.util.List;

import static ru.nazimov.BankAccounts.util.ErrorUtil.getErrorMessage;
import static ru.nazimov.BankAccounts.util.ErrorUtil.getHttpStatus;


@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountsController {
    private final AccountsService accountsService;

    @GetMapping
    public ResponseEntity<List<AccountDtoResponse>> getAccounts() {
        return new ResponseEntity<>(accountsService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid AccountDtoCreation accountDto) {
        accountsService.create(accountDto);

        return new ResponseEntity<>("Счет создан", HttpStatus.CREATED);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<HttpStatus> deposit(@RequestBody @Valid AccountDtoOperation accountDto) {
        accountsService.deposit(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdraw(@RequestBody @Valid AccountDtoOperation accountDto) {
        accountsService.withdraw(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/transfer")
    public ResponseEntity<HttpStatus> transfer(@RequestBody @Valid AccountDtoTransfer accountDto) {
        accountsService.transfer(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

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
