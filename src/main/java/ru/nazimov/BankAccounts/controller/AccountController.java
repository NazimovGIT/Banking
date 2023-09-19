package ru.nazimov.BankAccounts.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nazimov.BankAccounts.dto.*;
import ru.nazimov.BankAccounts.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDtoResponse>> getAccounts() {

        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid AccountDtoCreation accountDto) {
        accountService.create(accountDto);

        return new ResponseEntity<>("Счет создан", HttpStatus.CREATED);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<HttpStatus> deposit(@RequestBody @Valid AccountDtoOperation accountDto) {
        accountService.deposit(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdraw(@RequestBody @Valid AccountDtoOperation accountDto) {
        accountService.withdraw(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/transfer")
    public ResponseEntity<HttpStatus> transfer(@RequestBody @Valid AccountDtoTransfer accountDto) {
        accountService.transfer(accountDto);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
