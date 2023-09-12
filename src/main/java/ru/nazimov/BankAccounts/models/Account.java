package ru.nazimov.BankAccounts.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Account {
    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "pin")
    private String pin;
    @Column(name = "number")
    private String number;
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;
}
