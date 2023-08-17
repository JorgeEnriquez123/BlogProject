package com.jorge.blogproject.model.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRequest(
        @Size(min = 5, max = 30, message = "El usuario debe contener entre 5 a 30 caracteres")
        String username,
        @NotNull(message = "Se debe de ingresar un correo válido")
        @Email(message = "Se debe de ingresar un correo válido")
        String email,
        @NotNull(message = "Debe de ingresar una contraseña")
        String password,
        List<String> roles
){}
