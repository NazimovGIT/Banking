package ru.nazimov.BankAccounts.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nazimov.BankAccounts.dto.AccountDtoCreation;
import ru.nazimov.BankAccounts.dto.AccountDtoOperation;
import ru.nazimov.BankAccounts.dto.AccountDtoResponse;
import ru.nazimov.BankAccounts.dto.AccountDtoTransfer;
import ru.nazimov.BankAccounts.models.Account;
import ru.nazimov.BankAccounts.repositories.AccountsRepository;
import ru.nazimov.BankAccounts.util.AccountUtil;
import ru.nazimov.BankAccounts.util.AccountValidator;
import ru.nazimov.BankAccounts.util.Exceptions.AccountNotFoundException;
import ru.nazimov.BankAccounts.util.Exceptions.AccountOperationException;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountsService {
    private final AccountsRepository repository;
    private final ModelMapper mapper;
    private final AccountValidator validator;

    @Transactional
    public Account create(AccountDtoCreation dtoCreation) {
        validator.validate(dtoCreation);
        Account account = mapper.map(dtoCreation, Account.class);
        account.setNumber(AccountUtil.getAccountNumber());
        return repository.save(account);
    }

    @Transactional
    public Account deposit(AccountDtoOperation dtoOperation) {
        Account account = validator.validate(dtoOperation);
        account.setBalance(account.getBalance().add(dtoOperation.getAmount()));

        return repository.save(account);
    }

    @Transactional
    public Account withdraw(AccountDtoOperation dtoOperation) {
        Account account = validator.validate(dtoOperation);
        BigDecimal newBalance = account.getBalance().subtract(dtoOperation.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountOperationException("Недостаточно средств на счете");
        }
        account.setBalance(newBalance);

        return repository.save(account);
    }

    @Transactional
    public boolean transfer(AccountDtoTransfer dtoTransfer) {
        AccountDtoOperation dtoOperation = mapper.map(dtoTransfer, AccountDtoOperation.class);
        Optional<Account> accountToDepositOp = repository.findByName(dtoTransfer.getNameToTransfer());
        if (accountToDepositOp.isEmpty()) {
            throw new AccountNotFoundException("Счета для зачисления с таким именем не существует");
        }
        Account accountToDeposit = accountToDepositOp.get();

        withdraw(dtoOperation);
        accountToDeposit.setBalance(accountToDeposit.getBalance().add(dtoTransfer.getAmount()));
        repository.save(accountToDeposit);
        return true;
    }

    @Transactional(readOnly = true)
    public List<AccountDtoResponse> findAll() {
        List<Account> accounts = repository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("Не создано ни одного счета");
        }
        return accounts.stream()
                .map(account -> mapper.map(account, AccountDtoResponse.class))
                .collect(Collectors.toList());
    }
}
