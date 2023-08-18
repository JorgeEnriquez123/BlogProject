package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.Request.UserUpdateRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.service.RoleService;
import com.jorge.blogproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;
    RoleService roleService;
    PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }
    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(userService.findAllPaginable(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<UserEntity> userFound = userService.findById(id);
        if(userFound.isPresent()){
            return ResponseEntity.ok().body(userFound.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    // Puede aceptar solo 1 atributo o actualizar todos
    // NO permite actualizar rol - El rol se puede eliminar o asignar manualmente con otros endpoints
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userRequest, BindingResult result){
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        UserEntity userEntity = userService.findById(id).orElse(null);
        if(userEntity != null){
            userEntity.setUsername(userRequest.username());
            userEntity.setEmail(userRequest.email());

            String hashedPassword = passwordEncoder.encode(userRequest.password());
            userEntity.setPassword(hashedPassword);
            return ResponseEntity.ok().body(userService.save(userEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con el ID: " + id + " no encontrado.");
    }

    @PostMapping("/{userid}/roles/{roleid}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userid, @PathVariable Long roleid){
        Optional<UserEntity> userFound = userService.findById(userid);
        if (userFound.isPresent()) {
            UserEntity userEntity = userFound.get();
            Optional<RoleEntity> roleFound = roleService.findById(roleid);
            if(roleFound.isPresent()){
                RoleEntity roleEntity = roleFound.get();
                userEntity.getRoles().add(roleEntity);
                userService.save(userEntity);
                return ResponseEntity.ok().body(userEntity);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    // Delete Role of User
    @DeleteMapping("/{userid}/roles/{roleid}/delete")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userid, @PathVariable Long roleid){
        Optional<UserEntity> userFound = userService.findById(userid);
        if (userFound.isPresent()) {
            UserEntity userEntity = userFound.get();
            Optional<RoleEntity> roleFound = roleService.findById(roleid);
            if(roleFound.isPresent()){
                RoleEntity roleEntity = roleFound.get();
                userEntity.getRoles().remove(roleEntity);
                userService.save(userEntity);
                // Usuario con el rol removido guardado
                return ResponseEntity.ok().body(userEntity);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id){
        UserEntity userDeleted = userService.delete(id);
        if(userDeleted != null){
            return ResponseEntity.ok().body(userDeleted);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        UserEntity userRestored = userService.restore(id);
        if(userRestored != null){
            return ResponseEntity.ok().body(userRestored);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }
}
