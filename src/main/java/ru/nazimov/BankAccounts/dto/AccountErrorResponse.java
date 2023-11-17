package ru.nazimov.BankAccounts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class AccountErrorResponse {

    private String message;
    private Map<String, String> validationErrors;
    private LocalDateTime dateTime;

    public AccountErrorResponse(String message) {
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
