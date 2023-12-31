package com.jorge.blogproject.model.Request;

import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(
        @Size(min = 5, max = 30, message = "El usuario debe contener entre 5 a 30 caracteres")
        String username,
        @NotNull(message = "Se debe de ingresar un correo válido")
        @Email(message = "Se debe de ingresar un correo válido")
        String email,
        @NotNull(message = "Debe de ingresar una contraseña")
        String password,
        @NotEmpty(message = "Debe de agregar un rol")
        List<String> roles
){}
