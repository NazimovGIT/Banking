package ru.nazimov.BankAccounts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountErrorResponse {
    private String message;
    private long timestamp;

    public AccountErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
