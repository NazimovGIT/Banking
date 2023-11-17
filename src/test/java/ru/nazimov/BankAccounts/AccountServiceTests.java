package ru.nazimov.BankAccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.exception.*;
import ru.nazimov.BankAccounts.mappers.AccountMapper;
import ru.nazimov.BankAccounts.model.Account;
import ru.nazimov.BankAccounts.repository.AccountRepository;
import ru.nazimov.BankAccounts.service.AccountService;
import ru.nazimov.BankAccounts.util.AccountValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.nazimov.BankAccounts.util.AccountUtil.getAccountNumber;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository repository;
    @Mock
    private AccountValidator validator;
    @Mock
    private AccountMapper mapper;
    @InjectMocks
    private AccountService service;
    private AccountDto dtoCreation;
    private AccountDto dtoOperation;

    @BeforeEach
    public void setup() {
        dtoCreation = AccountDto.builder()
                .name("dtoCreation")
                .pin("1234")
                .build();
        dtoOperation = AccountDto.builder()
                .name("dtoOperation")
                .pin("5678")
                .amount(BigDecimal.valueOf(30))
                .build();
    }

    @Test
    public void whenCreateAccount_thenReturnCreatedAccount() {
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoCreation.getName())
                .pin(dtoCreation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();

        doNothing().when(validator).validateToCreate(dtoCreation);
        when(mapper.toAccount(dtoCreation)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.create(dtoCreation);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(validator).validateToCreate(dtoCreation);
        verify(mapper).toAccount(dtoCreation);
        verify(repository).save(expectedAccount);
    }

    @Test
    public void whenCreateAccountWithExistingAccountName_thenThrowAccountNotCreatedException() {
        doThrow(AccountNotCreatedException.class).when(validator).validateToCreate(dtoCreation);

        assertThrows(AccountNotCreatedException.class, (() ->
                validator.validateToCreate(dtoCreation)));

        verify(validator).validateToCreate(dtoCreation);
        verify(mapper, never()).toAccount(dtoCreation);
        verify(repository, never()).save(mapper.toAccount(dtoCreation));
    }

    @Test
    public void whenDepositToExistingAccount_thenReturnDepositedAccount() {
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();

        when(validator.validateToOperation(dtoOperation)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.deposit(dtoOperation);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(validator).validateToOperation(dtoOperation);
        verify(repository).save(expectedAccount);
    }

    @Test
    public void whenDepositToNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validateToOperation(dtoOperation);

        assertThrows(AccountException.class, (() -> validator.validateToOperation(dtoOperation)));

        verify(validator).validateToOperation(dtoOperation);
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenDepositWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validateToOperation(dtoOperation);

        assertThrows(AccountAuthorizationException.class, (() -> validator.validateToOperation(dtoOperation)));

        verify(validator).validateToOperation(dtoOperation);
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenWithdrawFromExistingAccount_thenReturnWithdrawnAccount() {
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();

        when(validator.validateToOperation(dtoOperation)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.withdraw(dtoOperation);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(validator).validateToOperation(dtoOperation);
        verify(repository).save(expectedAccount);
    }

    @Test
    public void whenWithdrawFromNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validateToOperation(dtoOperation);

        assertThrows(AccountException.class, (() -> validator.validateToOperation(dtoOperation)));

        verify(validator).validateToOperation(dtoOperation);
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenWithdrawWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validateToOperation(dtoOperation);

        assertThrows(AccountAuthorizationException.class, (() -> validator.validateToOperation(dtoOperation)));

        verify(validator).validateToOperation(dtoOperation);
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenWithdrawFromAccountWithInSufficientBalance_thenThrowAccountOperationException() {
        Account account = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(20))
                .build();

        when(validator.validateToOperation(dtoOperation)).thenReturn(account);

        assertThrows(AccountOperationException.class, (() -> service.withdraw(dtoOperation)));

        verify(validator).validateToOperation(dtoOperation);
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenTransferBetweenAccounts_thenSuccess() {
        AccountDto dtoTransfer = AccountDto.builder()
                .name("ExampleAccountName")
                .pin("1234")
                .nameToTransfer("ExampleAccountNameToTransfer")
                .amount(BigDecimal.valueOf(30))
                .build();
        Account accountToWithdraw = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoTransfer.getName())
                .pin(dtoTransfer.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();
        Account accountToDeposit = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoTransfer.getNameToTransfer())
                .pin(dtoTransfer.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(0))
                .build();

        when(repository.findByName(dtoTransfer.getNameToTransfer())).thenReturn(Optional.of(accountToDeposit));
        when(validator.validateToOperation(dtoTransfer)).thenReturn(accountToWithdraw);
        when(repository.save(accountToWithdraw)).thenReturn(accountToWithdraw);
        when(repository.save(accountToDeposit)).thenReturn(accountToDeposit);

        service.transfer(dtoTransfer);

        verify(repository).findByName(dtoTransfer.getNameToTransfer());
        verify(validator).validateToOperation(dtoTransfer);
        verify(repository).save(accountToWithdraw);
        verify(repository).save(accountToDeposit);
    }

    @Test
    public void whenTransferToNotExistingAccount_thenThrowAccountNotFoundException() {
        AccountDto dtoTransfer = AccountDto.builder()
                .name("ExampleAccountName")
                .pin("1234")
                .nameToTransfer("ExampleAccountNameToTransfer")
                .amount(BigDecimal.valueOf(30))
                .build();
        dtoOperation.setName(dtoTransfer.getName());
        dtoOperation.setPin(dtoTransfer.getPin());
        dtoOperation.setAmount(dtoTransfer.getAmount());

        when(repository.findByName(dtoTransfer.getNameToTransfer())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, (() -> service.transfer(dtoTransfer)));

        verify(repository).findByName(dtoTransfer.getNameToTransfer());
        verify(validator, never()).validateToOperation(dtoOperation);
        verify(repository, never()).findByName(dtoOperation.getName());
        verify(repository, never()).save(mapper.toAccount(dtoOperation));
    }

    @Test
    public void whenFindAllAccounts_thenReturnListOfAccountDtoObjects() {
        Account account1 = Account.builder()
                .uuid(UUID.randomUUID())
                .name("ExampleName1")
                .pin("1234")
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(10))
                .build();
        Account account2 = Account.builder()
                .uuid(UUID.randomUUID())
                .name("ExampleName2")
                .pin("5678")
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(0))
                .build();
        AccountDto dtoResponse1 = AccountDto.builder()
                .name(account1.getName())
                .balance(account1.getBalance())
                .build();
        AccountDto dtoResponse2 = AccountDto.builder()
                .name(account2.getName())
                .balance(account2.getBalance())
                .build();
        List<Account> accounts = List.of(account1, account2);
        when(repository.findAll()).thenReturn(accounts);

        List<AccountDto> expected = List.of(dtoResponse1, dtoResponse2);

        when(mapper.toListDto(accounts)).thenReturn(expected);

        List<AccountDto> actual = service.findAll();

        assertThat(actual).isEqualTo(expected);

        verify(repository).findAll();
        verify(mapper).toListDto(accounts);
    }

    @Test
    public void whenFindAllAccountsNotPresent_thenThrowsAccountNotFoundException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(AccountNotFoundException.class, (() -> service.findAll()));

        verify(repository).findAll();
        verify(mapper, never()).toDto(new Account());
        verify(mapper, never()).toDto(new Account());
    }
}
