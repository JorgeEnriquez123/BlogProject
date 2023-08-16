package com.jorge.blogproject.model.Request;

import java.util.List;

public record UserRequest(
        String username,
        String email,
        String password,
        List<String> roles
){}
