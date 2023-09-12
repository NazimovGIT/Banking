package ru.nazimov.BankAccounts.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AccountDtoOperation {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Имя должно состоять из буквенно-цифровых символов")
    private String name;

    @NotEmpty(message = "ПИН-код не должен быть пустым")
    @Pattern(regexp = "\\d{4}", message = "ПИН-код должен состоять из 4 чисел")
    private String pin;
    @NotNull(message = "Сумма операции не должно быть пустым")
    @DecimalMin(value = "0.00", inclusive = false, message = "Сумма операции должна быть больше 0.00")
    @Digits(integer = 100, fraction = 2, message = "Сумма операции должна быть с точностью до копейки")
    private BigDecimal amount;
}
