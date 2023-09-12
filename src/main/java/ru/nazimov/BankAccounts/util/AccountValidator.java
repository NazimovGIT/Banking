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


@Component
@AllArgsConstructor
public class AccountValidator {
    private final AccountsRepository accountsRepository;

    public void validate(AccountDtoCreation accountDto) {
        if (accountsRepository.findByName(accountDto.getName()).isPresent()) {
            throw new AccountNotCreatedException("Счет с таким именем уже существует");
        }
    }

    public void validate(AccountDtoOperation accountDto) {
        Optional<Account> account = accountsRepository.findByName(accountDto.getName());
        if (account.isEmpty()) {
            throw new AccountException("Счета с таким именем не существует");
        } else if (!account.get().getPin().equals(accountDto.getPin())) {
            throw new AccountAuthorizationException("Неверный ПИН-код");
        }
    }
}
