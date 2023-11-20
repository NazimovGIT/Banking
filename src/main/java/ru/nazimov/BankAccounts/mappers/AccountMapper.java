package ru.nazimov.BankAccounts.mappers;

import org.mapstruct.Mapper;
import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.model.Account;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto accountDto);

    AccountDto toDto(Account account);

    List<AccountDto> toListDto(List<Account> accounts);

}