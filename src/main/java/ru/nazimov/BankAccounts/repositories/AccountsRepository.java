package ru.nazimov.BankAccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nazimov.BankAccounts.models.Account;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByName(String name);
}
