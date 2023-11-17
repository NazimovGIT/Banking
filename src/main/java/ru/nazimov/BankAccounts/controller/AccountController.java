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
import java.util.UUID;

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
    public ResponseEntity<AccountDto> create(@Validated(value = OnCreate.class) @RequestBody AccountDto accountDto) {
        accountDto = accountService.create(accountDto);
        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getById(@PathVariable UUID id){
        AccountDto accountDto = accountService.getById(id);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<AccountDto> deposit(@Validated(value = OnOperation.class) @RequestBody AccountDto accountDto) {
        accountDto = accountService.deposit(accountDto);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<AccountDto> withdraw(@Validated(value = OnOperation.class) @RequestBody AccountDto accountDto) {
        accountDto = accountService.withdraw(accountDto);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/transfer")
    public ResponseEntity<AccountDto> transfer(@Validated(value = OnTransfer.class) @RequestBody AccountDto accountDto) {
        accountDto = accountService.transfer(accountDto);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @DeleteMapping()
    public void delete(@RequestBody @Validated(value = OnCreate.class) AccountDto accountDto){
        accountService.delete(accountDto);
    }

}
