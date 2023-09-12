package ru.nazimov.BankAccounts.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nazimov.BankAccounts.dto.AccountDtoCreation;
import ru.nazimov.BankAccounts.dto.AccountDtoOperation;
import ru.nazimov.BankAccounts.models.Account;
import ru.nazimov.BankAccounts.repositories.AccountsRepository;
import ru.nazimov.BankAccounts.util.Exceptions.AccountAuthorizationException;
import ru.nazimov.BankAccounts.util.Exceptions.AccountException;
import ru.nazimov.BankAccounts.util.Exceptions.AccountNotCreatedException;

import java.util.Optional;
import java.util.function.Supplier;


@Component
@AllArgsConstructor
public class AccountValidator {
    private final AccountsRepository accountsRepository;

    public void validate(AccountDtoCreation accountDto) {
        if (accountsRepository.findByName(accountDto.getName()).isPresent()) {
            throw new AccountNotCreatedException("Счет с таким именем уже существует");
        }
    }

    public Account validate(AccountDtoOperation accountDto) {
        Optional<Account> accountOp = accountsRepository.findByName(accountDto.getName());

        Account account = accountOp.orElseThrow(()->new AccountException("Счета с таким именем не существует"));

        if (!account.getPin().equals(accountDto.getPin())) {
            throw new AccountAuthorizationException("Неверный ПИН-код");
        }
        return account;
    }
}
