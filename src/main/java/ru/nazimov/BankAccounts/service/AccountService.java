package ru.nazimov.BankAccounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.exception.AccountNotFoundException;
import ru.nazimov.BankAccounts.exception.AccountOperationException;
import ru.nazimov.BankAccounts.mapper.Mapper;
import ru.nazimov.BankAccounts.model.Account;
import ru.nazimov.BankAccounts.repository.AccountRepository;
import ru.nazimov.BankAccounts.util.AccountUtil;
import ru.nazimov.BankAccounts.util.AccountValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final Mapper<Account, AccountDto> mapper;
    private final AccountValidator validator;

    @Transactional
    public AccountDto create(AccountDto dtoCreation) {
        validator.validateToCreate(dtoCreation);
        Account account = mapper.toEntity(dtoCreation);
        account.setNumber(AccountUtil.getAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        return mapper.toDto(repository.save(account));
    }

    @Transactional(readOnly = true)
    public AccountDto getById(UUID id) {
        Account account = repository.findById(id).orElseThrow(() ->
                new AccountNotFoundException("Счет с таким идентификатором не существует."));

        return mapper.toDto(account);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccounts() {
        List<Account> accounts = repository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("Не создано ни одного счета");
        }
        return mapper.toListDto(accounts);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByBalanceAtLeast(BigDecimal balance) {
        List<Account> accounts = repository.findAllByBalanceAtLeast(balance);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("Счета с балансом больше указанного не существует.");
        }
        return mapper.toListDto(accounts);
    }

    @Transactional
    public AccountDto deposit(AccountDto dtoOperation) {
        Account account = validator.validateToOperation(dtoOperation);
        account.setBalance(account.getBalance().add(dtoOperation.getAmount()));

        return mapper.toDto(repository.save(account));
    }

    @Transactional
    public AccountDto withdraw(AccountDto dtoOperation) {
        Account account = validator.validateToOperation(dtoOperation);
        BigDecimal newBalance = account.getBalance().subtract(dtoOperation.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountOperationException("Недостаточно средств на счете");
        }
        account.setBalance(newBalance);

        return mapper.toDto(repository.save(account));
    }

    @Transactional
    public AccountDto transfer(AccountDto dtoTransfer) {
        Optional<Account> accountToDepositOp = repository.findByName(dtoTransfer.getNameToTransfer());
        Account accountToDeposit = accountToDepositOp.orElseThrow(() ->
                new AccountNotFoundException("Счета для зачисления с таким именем не существует"));

        AccountDto withdrawnAccountDto = withdraw(dtoTransfer);
        accountToDeposit.setBalance(accountToDeposit.getBalance().add(dtoTransfer.getAmount()));

        return withdrawnAccountDto;
    }

    @Transactional
    public void delete(AccountDto accountDto) {
        Account account = validator.validateToOperation(accountDto);

        repository.delete(account);
    }

}
