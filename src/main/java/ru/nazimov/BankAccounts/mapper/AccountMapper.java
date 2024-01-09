package ru.nazimov.BankAccounts.mapper;

import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.model.Account;

@org.mapstruct.Mapper(componentModel = "spring")
public interface AccountMapper extends Mapper<Account, AccountDto> {

}