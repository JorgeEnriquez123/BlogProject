package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.Request.LoginRequest;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.RolService;
import com.jorge.blogproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    UserService userService;
    RolService rolService;
    PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, RolService rolService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        UserEntity user = userService.login(loginRequest);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
        return ResponseEntity.ok().body("Logueado correctamente");
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest userRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.username());
        userEntity.setEmail(userRequest.email());

        String hashedPassword = passwordEncoder.encode(userRequest.password());
        userEntity.setPassword(hashedPassword);

        Set<RoleEntity> roles = userRequest.roles().stream()
                .map(rol -> {
                    EnumRole enumRole = EnumRole.valueOf("ROLE_" + rol);
                    RoleEntity role = rolService.findByName(enumRole);
                    if(role == null){
                        role = new RoleEntity();
                        role.setName(enumRole);
                    }
                    return role;
                }).collect(Collectors.toSet());
        userEntity.setRoles(roles);
        return ResponseEntity.ok().body(userService.save(userEntity));
    }
}
