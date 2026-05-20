package com.dreamtech.api_gateway.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterUserDto {
    @NotBlank(message = "El nombre de usuario es requerido")
    private String username;
    @NotBlank(message = "La contraseña es requerido")
    @Length(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;
    @NotBlank(message = "La confirmación de contraseña es requerido")
    @Length(min = 8, max = 20, message = "La confirmación de contraseña debe tener entre 8 y 20 caracteres")
    private String confirmPassword;
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
}
