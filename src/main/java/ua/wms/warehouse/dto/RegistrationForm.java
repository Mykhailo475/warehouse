package ua.wms.warehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationForm {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private String patronymic;
    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;
    private Long telephone;
}
