package ru.nazimov.BankAccounts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nazimov.BankAccounts.dto.*;
import ru.nazimov.BankAccounts.dto.validation.OnCreate;
import ru.nazimov.BankAccounts.dto.validation.OnOperation;
import ru.nazimov.BankAccounts.dto.validation.OnTransfer;
import ru.nazimov.BankAccounts.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {

        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> create(@Validated(value = OnCreate.class) @RequestBody AccountDto accountDto) {
        accountService.create(accountDto);

        return new ResponseEntity<>("Счет создан", HttpStatus.CREATED);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<HttpStatus> deposit(@Validated(value = OnOperation.class) @RequestBody AccountDto accountDto) {
        accountService.deposit(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdraw(@Validated(value = OnOperation.class) @RequestBody AccountDto accountDto) {
        accountService.withdraw(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/transfer")
    public ResponseEntity<HttpStatus> transfer(@Validated(value = OnTransfer.class) @RequestBody AccountDto accountDto) {
        accountService.transfer(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public void delete(@RequestBody @Validated(value = OnCreate.class) AccountDto accountDto){
        accountService.delete(accountDto);
    }

}
