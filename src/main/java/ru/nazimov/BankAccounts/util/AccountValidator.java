package ru.nazimov.BankAccounts.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nazimov.BankAccounts.dto.AccountDtoCreation;
import ru.nazimov.BankAccounts.dto.AccountDtoOperation;
import ru.nazimov.BankAccounts.model.Account;
import ru.nazimov.BankAccounts.repository.AccountRepository;
import ru.nazimov.BankAccounts.exception.AccountAuthorizationException;
import ru.nazimov.BankAccounts.exception.AccountException;
import ru.nazimov.BankAccounts.exception.AccountNotCreatedException;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountValidator {
    private final AccountRepository accountRepository;

    public void validate(AccountDtoCreation accountDto) {
        if (accountRepository.existsByName(accountDto.getName())) {
            throw new AccountNotCreatedException("Счет с таким именем уже существует");
        }
    }

    public Account validate(AccountDtoOperation accountDto) {
        Optional<Account> accountOp = accountRepository.findByName(accountDto.getName());

        Account account = accountOp.orElseThrow(()->new AccountException("Счета с таким именем не существует"));

        if (!account.getPin().equals(accountDto.getPin())) {
            throw new AccountAuthorizationException("Неверный ПИН-код");
        }
        return account;
    }
}
