package ru.nazimov.BankAccounts.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nazimov.BankAccounts.dto.AccountDtoCreation;
import ru.nazimov.BankAccounts.dto.AccountDtoOperation;
import ru.nazimov.BankAccounts.dto.AccountDtoResponse;
import ru.nazimov.BankAccounts.dto.AccountDtoTransfer;
import ru.nazimov.BankAccounts.model.Account;
import ru.nazimov.BankAccounts.repository.AccountRepository;
import ru.nazimov.BankAccounts.util.AccountUtil;
import ru.nazimov.BankAccounts.util.AccountValidator;
import ru.nazimov.BankAccounts.exception.AccountNotFoundException;
import ru.nazimov.BankAccounts.exception.AccountOperationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository repository;
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
    public void transfer(AccountDtoTransfer dtoTransfer) {
        AccountDtoOperation dtoOperation = mapper.map(dtoTransfer, AccountDtoOperation.class);
        Optional<Account> accountToDepositOp = repository.findByName(dtoTransfer.getNameToTransfer());
        Account accountToDeposit = accountToDepositOp.orElseThrow(() ->
                new AccountNotFoundException("Счета для зачисления с таким именем не существует"));

        withdraw(dtoOperation);
        accountToDeposit.setBalance(accountToDeposit.getBalance().add(dtoTransfer.getAmount()));
        repository.save(accountToDeposit);
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
