package ru.nazimov.BankAccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


import org.modelmapper.ModelMapper;
import ru.nazimov.BankAccounts.dto.AccountDtoCreation;
import ru.nazimov.BankAccounts.dto.AccountDtoOperation;
import ru.nazimov.BankAccounts.dto.AccountDtoResponse;
import ru.nazimov.BankAccounts.dto.AccountDtoTransfer;
import ru.nazimov.BankAccounts.models.Account;
import ru.nazimov.BankAccounts.repositories.AccountsRepository;
import ru.nazimov.BankAccounts.services.AccountsService;
import ru.nazimov.BankAccounts.util.AccountValidator;
import ru.nazimov.BankAccounts.util.Exceptions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.nazimov.BankAccounts.util.AccountUtil.getAccountNumber;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceTests {
    @Mock
    private AccountsRepository repository;
    @Mock
    private AccountValidator validator;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private AccountsService service;
    private AccountDtoCreation dtoCreation;
    private AccountDtoOperation dtoOperation;
    @BeforeEach
    public void setup(){
        dtoCreation = AccountDtoCreation.builder()
                .name("dtoCreation")
                .pin("1234")
                .build();
        dtoOperation = AccountDtoOperation.builder()
                .name("dtoOperation")
                .pin("5678")
                .amount(BigDecimal.valueOf(30))
                .build();
    }
    @Test
    public void whenCreateAccount_thenReturnCreatedAccount(){
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoCreation.getName())
                .pin(dtoCreation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();

        doNothing().when(validator).validate(dtoCreation);
        when(mapper.map(dtoCreation, Account.class)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.create(dtoCreation);

        assertThat(actualAccount).isEqualTo(expectedAccount);
        assertThat(actualAccount.getUuid()).isNotNull();
        assertThat(actualAccount.getNumber()).isNotNull();

        verify(validator, times(1)).validate(dtoCreation);
        verify(mapper, times(1)).map(dtoCreation, Account.class);
        verify(repository, times(1)).save(expectedAccount);
    }
    @Test
    public void whenCreateAccountWithExistingAccountName_thenThrowAccountNotCreatedException(){
        doThrow(AccountNotCreatedException.class).when(validator).validate(dtoCreation);

        assertThrows(AccountNotCreatedException.class, (() ->
            validator.validate(dtoCreation)));

        verify(validator, times(1)).validate(dtoCreation);
        verify(mapper, never()).map(dtoCreation, Account.class);
        verify(repository, never()).save(mapper.map(dtoCreation, Account.class));
    }
    @Test
    public void whenDepositToExistingAccount_thenReturnDepositedAccount(){
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();

        when(validator.validate(dtoOperation)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.deposit(dtoOperation);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, times(1)).save(expectedAccount);
    }
    @Test
    public void whenDepositToNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validate(dtoOperation);

        assertThrows(AccountException.class, (() -> validator.validate(dtoOperation)));

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenDepositWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validate(dtoOperation);

        assertThrows(AccountAuthorizationException.class, (() -> validator.validate(dtoOperation)));

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenWithdrawFromExistingAccount_thenReturnWithdrawnAccount(){
        Account expectedAccount = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();

        when(validator.validate(dtoOperation)).thenReturn(expectedAccount);
        when(repository.save(expectedAccount)).thenReturn(expectedAccount);

        Account actualAccount = service.withdraw(dtoOperation);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, times(1)).save(expectedAccount);
    }
    @Test
    public void whenWithdrawFromNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validate(dtoOperation);

        assertThrows(AccountException.class, (() -> validator.validate(dtoOperation)));

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenWithdrawWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validate(dtoOperation);

        assertThrows(AccountAuthorizationException.class, (() -> validator.validate(dtoOperation)));

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenWithdrawFromAccountWithNotSufficientBalance_thenThrowAccountOperationException() {
        Account account = Account.builder()
                .uuid(UUID.randomUUID())
                .name(dtoOperation.getName())
                .pin(dtoOperation.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(20))
                .build();

        when(validator.validate(dtoOperation)).thenReturn(account);

        assertThrows(AccountOperationException.class, (() -> service.withdraw(dtoOperation)));

        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenTransferBetweenAccounts_thenReturnTrue(){
        AccountDtoTransfer dtoTransfer = AccountDtoTransfer.builder()
                .name("ExampleAccountName")
                .pin("1234")
                .nameToTransfer("ExampleAccountNameToTransfer")
                .amount(BigDecimal.valueOf(30))
                .build();
        dtoOperation.setName(dtoTransfer.getName());
        dtoOperation.setPin(dtoTransfer.getPin());
        dtoOperation.setAmount(dtoTransfer.getAmount());
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

        when(mapper.map(dtoTransfer, AccountDtoOperation.class)).thenReturn(dtoOperation);
        when(repository.findByName(dtoTransfer.getNameToTransfer())).thenReturn(Optional.of(accountToDeposit));
        when(validator.validate(dtoOperation)).thenReturn(accountToWithdraw);
        when(repository.save(accountToWithdraw)).thenReturn(accountToWithdraw);
        when(repository.save(accountToDeposit)).thenReturn(accountToDeposit);

        assertTrue(service.transfer(dtoTransfer));

        verify(mapper, times(1)).map(dtoTransfer, AccountDtoOperation.class);
        verify(repository, times(1)).findByName(dtoTransfer.getNameToTransfer());
        verify(validator, times(1)).validate(dtoOperation);
        verify(repository, times(1)).save(accountToWithdraw);
        verify(repository, times(1)).save(accountToDeposit);
    }
    @Test
    public void whenTransferToNotExistingAccount_thenThrowAccountNotFoundException() {
        AccountDtoTransfer dtoTransfer = AccountDtoTransfer.builder()
                .name("ExampleAccountName")
                .pin("1234")
                .nameToTransfer("ExampleAccountNameToTransfer")
                .amount(BigDecimal.valueOf(30))
                .build();
        dtoOperation.setName(dtoTransfer.getName());
        dtoOperation.setPin(dtoTransfer.getPin());
        dtoOperation.setAmount(dtoTransfer.getAmount());

        when(mapper.map(dtoTransfer, AccountDtoOperation.class)).thenReturn(dtoOperation);
        when(repository.findByName(dtoTransfer.getNameToTransfer())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, (() -> service.transfer(dtoTransfer)));

        verify(mapper, times(1)).map(dtoTransfer, AccountDtoOperation.class);
        verify(repository, times(1)).findByName(dtoTransfer.getNameToTransfer());
        verify(validator, never()).validate(dtoOperation);
        verify(repository, never()).findByName(dtoOperation.getName());
        verify(repository, never()).save(mapper.map(dtoOperation, Account.class));
    }
    @Test
    public void whenFindAllAccounts_thenReturnListOfAccountDtoResponseObjects(){
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
        AccountDtoResponse dtoResponse1 = AccountDtoResponse.builder()
                .name(account1.getName())
                .balance(account1.getBalance())
                .build();
        AccountDtoResponse dtoResponse2 = AccountDtoResponse.builder()
                .name(account2.getName())
                .balance(account2.getBalance())
                .build();
        List<Account> accounts = List.of(account1, account2);
        when(repository.findAll()).thenReturn(accounts);
        when(mapper.map(account1, AccountDtoResponse.class)).thenReturn(dtoResponse1);
        when(mapper.map(account2, AccountDtoResponse.class)).thenReturn(dtoResponse2);

        List<AccountDtoResponse> expected = List.of(dtoResponse1, dtoResponse2);
        List<AccountDtoResponse> actual = service.findAll();

        assertThat(actual).isEqualTo(expected);

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).map(account1, AccountDtoResponse.class);
        verify(mapper, times(1)).map(account2, AccountDtoResponse.class);
    }
    @Test
    public void whenFindAllAccountsNotPresent_thenThrowsAccountNotFoundException(){
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(AccountNotFoundException.class, (() -> service.findAll()));

        verify(repository, times(1)).findAll();
        verify(mapper, never()).map(new Account(), AccountDtoResponse.class);
        verify(mapper, never()).map(new Account(), AccountDtoResponse.class);
    }
}
