package ru.nazimov.BankAccounts.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AccountDtoResponse {
    private String name;
    private BigDecimal balance;
}
