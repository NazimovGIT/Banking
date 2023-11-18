package ru.nazimov.BankAccounts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nazimov.BankAccounts.dto.AccountDto;
import ru.nazimov.BankAccounts.model.Account;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "number", ignore = true)
    Account toAccount(AccountDto accountDto);

    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "nameToTransfer", ignore = true)
    AccountDto toDto(Account account);

    List<AccountDto> toListDto(List<Account> accounts);

}