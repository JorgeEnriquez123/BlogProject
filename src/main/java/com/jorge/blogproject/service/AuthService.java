package com.jorge.blogproject.service;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.Request.LoginRequest;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.repository.RoleRepository;
import com.jorge.blogproject.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserEntity login(LoginRequest loginRequest) {
        UserEntity userFound = userRepository.findByUsername(loginRequest.username());
        if (userFound == null || !passwordEncoder.matches(loginRequest.password(), userFound.getPassword())) {
            // User not found or User's password is incorrect
            return null;
        }
        return userFound; // User successfully authenticated
    }

    public UserEntity signup(@Valid @RequestBody UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.username());
        userEntity.setEmail(userRequest.email());

        String hashedPassword = passwordEncoder.encode(userRequest.password());
        userEntity.setPassword(hashedPassword);

        Set<RoleEntity> roles = userRequest.roles().stream()
                .map(rol -> {
                    EnumRole enumRole = EnumRole.valueOf("ROLE_" + rol);
                    RoleEntity role = roleRepository.findByName(enumRole);
                    if (role == null) {
                        role = new RoleEntity();
                        role.setName(enumRole);
                    }
                    return role;
                }).collect(Collectors.toSet());
        userEntity.setRoles(roles);
        return userRepository.save(userEntity);
    }
}
