package ru.nazimov.BankAccounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDtoCreation {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Имя должно состоять из буквенно-цифровых символов")
    private String name;

    @NotEmpty(message = "ПИН-код не должен быть пустым")
    @Pattern(regexp = "\\d{4}", message = "ПИН-код должен состоять из 4 чисел")
    private String pin;
}
