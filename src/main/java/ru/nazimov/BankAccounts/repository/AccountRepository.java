package ru.nazimov.BankAccounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nazimov.BankAccounts.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByName(String name);

    boolean existsByName(String name);

    @Query("select a from Account a where a.balance >= :balance")
    List<Account> findAllByBalanceAtLeast(@Param("balance") BigDecimal balance);

}
