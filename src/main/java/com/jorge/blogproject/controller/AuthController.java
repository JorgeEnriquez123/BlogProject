package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.Request.LoginRequest;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    AuthService authService;
    PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserEntity user = authService.login(loginRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
        return ResponseEntity.ok().body("Logueado correctamente");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            UserEntity userEntity = authService.signup(userRequest);
            return ResponseEntity.ok().body(userEntity);
        }
        catch (DataIntegrityViolationException d){
            LOG.error(d.getMessage());
            return ResponseEntity.badRequest().body("Username o email ya existentes");
        }
        catch (IllegalArgumentException e){
            LOG.error(e.getMessage());
            return ResponseEntity.badRequest().body("Rol o roles no validos");
        }
    }
}
