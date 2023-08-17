package com.jorge.blogproject.model.Request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String username,
        @NotNull
        String password
){}
