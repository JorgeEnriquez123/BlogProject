package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.RolService;
import com.jorge.blogproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;
    RolService rolService;
    PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RolService rolService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<UserEntity> userFound = userService.findById(id);
        if(userFound.isPresent()){
            return ResponseEntity.ok().body(userFound.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest userRequest){
        UserEntity userEntity = userService.findById(id).orElse(null);
        if(userEntity != null){
            if(userRequest.username() != null) userEntity.setUsername(userRequest.username());
            if(userRequest.email() != null) userEntity.setEmail(userRequest.email());
            if(userRequest.password() != null) userEntity.setPassword(userRequest.password());

            if(userRequest.roles() != null) {
                Set<RoleEntity> rolesFromRequest = userRequest.roles().stream()
                        .map(rolFromRequest -> {
                            EnumRole enumRole = EnumRole.valueOf("ROLE_" + rolFromRequest);
                            RoleEntity role = rolService.findByName(enumRole);
                            if (role == null) {
                                role = new RoleEntity();
                                role.setName(enumRole);
                            }
                            return role;
                        }).collect(Collectors.toSet());
                userEntity.setRoles(rolesFromRequest);
            }
            return ResponseEntity.ok().body(userService.save(userEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con el ID: " + id + " no encontrado.");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<UserEntity> userFound = userService.findById(id);
        if(userFound.isPresent()){
            userService.deleteById(userFound.get().getId());
            return ResponseEntity.ok().body("Usuario eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }
}
