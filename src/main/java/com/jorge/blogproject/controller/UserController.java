package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.PostService;
import com.jorge.blogproject.service.RolService;
import com.jorge.blogproject.service.UserService;
import jakarta.persistence.Entity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RolService rolService;

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

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserRequest userRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.username());
        userEntity.setEmail(userRequest.email());
        userEntity.setPassword(userRequest.password());

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
