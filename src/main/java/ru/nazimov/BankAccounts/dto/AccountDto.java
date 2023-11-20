package ru.nazimov.BankAccounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nazimov.BankAccounts.dto.validation.OnCreate;
import ru.nazimov.BankAccounts.dto.validation.OnOperation;
import ru.nazimov.BankAccounts.dto.validation.OnTransfer;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    @NotBlank(message = "Имя не должно быть пустым.", groups = {OnOperation.class, OnTransfer.class, OnCreate.class})
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов.", groups = {OnOperation.class, OnTransfer.class, OnCreate.class})
    @Pattern(regexp = "^[А-ЯA-Za-zа-я0-9]*$", message = "Имя должно состоять из буквенно-цифровых символов.", groups = {OnOperation.class, OnTransfer.class, OnCreate.class})
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "ПИН-код не должен быть пустым.", groups = {OnOperation.class, OnTransfer.class, OnCreate.class})
    @Pattern(regexp = "\\d{4}", message = "ПИН-код должен состоять из 4 чисел.", groups = {OnOperation.class, OnTransfer.class, OnCreate.class})
    private String pin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Сумма операции не должна быть пустой.", groups = {OnOperation.class, OnTransfer.class})
    @DecimalMin(value = "0.00", inclusive = false, message = "Сумма операции должна быть больше 0.00.", groups = {OnOperation.class, OnTransfer.class})
    @Digits(integer = 100, fraction = 2, message = "Сумма операции должна быть с точностью до копейки.", groups = {OnOperation.class, OnTransfer.class})
    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Имя для перевода не должно быть пустым.", groups = OnTransfer.class)
    @Size(min = 2, max = 30, message = "Имя для перевода должно содержать от 2 до 30 символов.", groups = OnTransfer.class)
    @Pattern(regexp = "^[А-ЯA-Za-zа-я0-9]*$", message = "Имя для перевода должно состоять из буквенно-цифровых символов.", groups = OnTransfer.class)
    private String nameToTransfer;

}
