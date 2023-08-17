package com.jorge.blogproject.model.Request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        String user
){}
