package com.dreamtech.api_gateway.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "El nombre de usuario es requerido")
    private String username;
    @NotBlank(message = "La contraseña es requerido")
    private String password;

}
