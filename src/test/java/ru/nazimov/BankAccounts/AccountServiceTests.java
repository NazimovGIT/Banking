package ru.nazimov.BankAccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.exception.AccountAuthorizationException;
import ru.nazimov.BankAccounts.exception.AccountException;
import ru.nazimov.BankAccounts.exception.AccountNotCreatedException;
import ru.nazimov.BankAccounts.exception.AccountNotFoundException;
import ru.nazimov.BankAccounts.exception.AccountOperationException;
import ru.nazimov.BankAccounts.mapper.Mapper;
import ru.nazimov.BankAccounts.model.Account;
import ru.nazimov.BankAccounts.repository.AccountRepository;
import ru.nazimov.BankAccounts.service.AccountService;
import ru.nazimov.BankAccounts.util.AccountValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.nazimov.BankAccounts.util.AccountUtil.getAccountNumber;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository repository;
    @Mock
    private AccountValidator validator;
    @Mock
    private Mapper<Account, AccountDto> mapper;
    @InjectMocks
    private AccountService service;
    private AccountDto accountDto;

    @BeforeEach
    public void setup() {
        accountDto = AccountDto.builder()
                .name("dto")
                .pin("1234")
                .balance(BigDecimal.valueOf(100))
                .amount(BigDecimal.valueOf(30))
                .build();
    }

    @Test
    public void whenCreateAccount_thenReturnCreatedAccountDto() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountDto.getName())
                .pin(accountDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();
        doNothing().when(validator).validateToCreate(accountDto);
        when(mapper.toEntity(accountDto)).thenReturn(account);
        when(repository.save(account)).thenReturn(account);
        when(mapper.toDto(account)).thenReturn(accountDto);

        AccountDto actualAccountDto = service.create(accountDto);

        assertThat(actualAccountDto).isEqualTo(accountDto);
        verify(validator).validateToCreate(accountDto);
        verify(mapper).toEntity(accountDto);
        verify(repository).save(account);
        verify(mapper).toDto(account);
    }

    @Test
    public void whenGetById_ThenReturnAccountDto() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountDto.getName())
                .build();
        when(repository.findById(account.getId())).thenReturn(Optional.of(account));
        when(mapper.toDto(account)).thenReturn(accountDto);

        AccountDto actualAccountDto = service.getById(account.getId());

        assertThat(actualAccountDto).isEqualTo(accountDto);
        verify(repository).findById(account.getId());
        verify(mapper).toDto(account);
    }

    @Test
    public void whenGetById_ThenThrowAccountNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, (() ->
                service.getById(id)));
        verify(repository).findById(id);
        verify(mapper, never()).toDto(new Account());
    }

    @Test
    public void whenCreateAccountWithExistingAccountName_thenThrowAccountNotCreatedException() {
        doThrow(AccountNotCreatedException.class).when(validator).validateToCreate(accountDto);

        assertThrows(AccountNotCreatedException.class, (() ->
                validator.validateToCreate(accountDto)));
        verify(validator).validateToCreate(accountDto);
        verify(mapper, never()).toEntity(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenDepositToExistingAccount_thenReturnDepositedAccountDto() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountDto.getName())
                .pin(accountDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();
        when(validator.validateToOperation(accountDto)).thenReturn(account);
        when(repository.save(account)).thenReturn(account);
        when(mapper.toDto(account)).thenReturn(accountDto);

        AccountDto actualAccountDto = service.deposit(accountDto);

        assertThat(actualAccountDto).isEqualTo(accountDto);
        verify(validator).validateToOperation(accountDto);
        verify(mapper).toDto(account);
        verify(repository).save(account);
    }

    @Test
    public void whenDepositToNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validateToOperation(accountDto);

        assertThrows(AccountException.class, (() ->
                validator.validateToOperation(accountDto)));
        verify(validator).validateToOperation(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenDepositWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validateToOperation(accountDto);

        assertThrows(AccountAuthorizationException.class, (() ->
                validator.validateToOperation(accountDto)));
        verify(validator).validateToOperation(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenWithdrawFromExistingAccount_thenReturnWithdrawnAccountDto() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountDto.getName())
                .pin(accountDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();
        when(validator.validateToOperation(accountDto)).thenReturn(account);
        when(repository.save(account)).thenReturn(account);
        when(mapper.toDto(account)).thenReturn(accountDto);

        AccountDto actualAccountDto = service.withdraw(accountDto);

        assertThat(actualAccountDto).isEqualTo(accountDto);
        verify(validator).validateToOperation(accountDto);
        verify(repository).save(account);
        verify(mapper).toDto(account);
    }

    @Test
    public void whenWithdrawFromNotExistingAccount_thenThrowAccountException() {
        doThrow(AccountException.class).when(validator).validateToOperation(accountDto);

        assertThrows(AccountException.class, (() ->
                validator.validateToOperation(accountDto)));
        verify(validator).validateToOperation(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenWithdrawWithInvalidPin_thenThrowAccountAuthorizationException() {
        doThrow(AccountAuthorizationException.class).when(validator).validateToOperation(accountDto);

        assertThrows(AccountAuthorizationException.class, (() ->
                validator.validateToOperation(accountDto)));
        verify(validator).validateToOperation(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenWithdrawFromAccountWithInSufficientBalance_thenThrowAccountOperationException() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name(accountDto.getName())
                .pin(accountDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(20))
                .build();
        when(validator.validateToOperation(accountDto)).thenReturn(account);

        assertThrows(AccountOperationException.class, (() ->
                service.withdraw(accountDto)));
        verify(validator).validateToOperation(accountDto);
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenTransferBetweenAccounts_thenReturnWithdrawnAccount() {
        AccountDto transferDto = accountDto;
        transferDto.setNameToTransfer("ExampleAccountNameToTransfer");
        Account accountToWithdraw = Account.builder()
                .id(UUID.randomUUID())
                .name(transferDto.getName())
                .pin(transferDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(50))
                .build();
        Account accountToDeposit = Account.builder()
                .id(UUID.randomUUID())
                .name(transferDto.getNameToTransfer())
                .pin(transferDto.getPin())
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(0))
                .build();
        when(repository.findByName(transferDto.getNameToTransfer()))
                .thenReturn(Optional.of(accountToDeposit));
        when(validator.validateToOperation(transferDto)).thenReturn(accountToWithdraw);
        when(repository.save(accountToWithdraw)).thenReturn(accountToWithdraw);
        when(mapper.toDto(accountToWithdraw)).thenReturn(transferDto);

        AccountDto actualAccountDto = service.transfer(transferDto);

        assertThat(actualAccountDto.getBalance()).isEqualTo(transferDto.getBalance());
        assertThat(accountToDeposit.getBalance()).isEqualTo(transferDto.getAmount());
        verify(repository).findByName(transferDto.getNameToTransfer());
        verify(validator).validateToOperation(transferDto);
        verify(repository).save(accountToWithdraw);
    }

    @Test
    public void whenTransferToNotExistingAccount_thenThrowAccountNotFoundException() {
        AccountDto transferDto = accountDto;
        transferDto.setNameToTransfer("ExampleAccountNameToTransfer");
        accountDto.setName(transferDto.getName());
        accountDto.setPin(transferDto.getPin());
        accountDto.setAmount(transferDto.getAmount());
        when(repository.findByName(transferDto.getNameToTransfer()))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, (() ->
                service.transfer(transferDto)));
        verify(repository).findByName(transferDto.getNameToTransfer());
        verify(validator, never()).validateToOperation(accountDto);
        verify(repository, never()).findByName(accountDto.getName());
        verify(repository, never()).save(mapper.toEntity(accountDto));
    }

    @Test
    public void whenFindAllAccounts_thenReturnListOfAccountDtoObjects() {
        Account account1 = Account.builder()
                .id(UUID.randomUUID())
                .name("ExampleName1")
                .pin("1234")
                .number(getAccountNumber())
                .balance(BigDecimal.valueOf(10))
                .build();
        Account account2 = Account.builder()
                .id(UUID.randomUUID())
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

        List<AccountDto> actual = service.getAccounts();

        assertThat(actual).isEqualTo(expected);
        verify(repository).findAll();
        verify(mapper).toListDto(accounts);
    }

    @Test
    public void whenFindAllAccountsNotPresent_thenThrowsAccountNotFoundException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(AccountNotFoundException.class, (() ->
                service.getAccounts()));
        verify(repository).findAll();
        verify(mapper, never()).toDto(new Account());
        verify(mapper, never()).toDto(new Account());
    }

    @Test
    public void deleteAccount() {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .build();
        when(validator.validateToOperation(accountDto)).thenReturn(account);

        service.delete(accountDto);

        verify(repository).delete(account);
    }
}
