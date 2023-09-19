package ru.nazimov.BankAccounts.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }
}
