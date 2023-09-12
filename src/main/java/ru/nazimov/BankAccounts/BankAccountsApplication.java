package ru.nazimov.BankAccounts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankAccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountsApplication.class, args);
    }

    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }

}
