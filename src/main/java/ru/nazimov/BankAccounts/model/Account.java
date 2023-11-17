package ru.nazimov.BankAccounts.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String name;

    private String pin;

    private String number;

    private BigDecimal balance;

}
