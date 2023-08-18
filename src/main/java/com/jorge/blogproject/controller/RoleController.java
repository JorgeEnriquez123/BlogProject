package com.jorge.blogproject.controller;

import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.service.RoleService;
import com.jorge.blogproject.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;
    UserService userService;
    PasswordEncoder passwordEncoder;

    public RoleController(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(roleService.findAll());
    }
    @GetMapping("/paginable")
    public ResponseEntity<?> findAllPaginable(@RequestParam int pageNumber, @RequestParam int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(roleService.findAllPaginable(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<RoleEntity> roleFound = roleService.findById(id);
        if(roleFound.isPresent()){
            return ResponseEntity.ok().body(roleFound.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
    }

    // Deshabilitar rol deshabilita para todos los usuarios
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id){
        RoleEntity roleDeleted = roleService.delete(id);
        if(roleDeleted != null){
            return ResponseEntity.ok().body(roleDeleted);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        RoleEntity roleRestored = roleService.restore(id);
        if(roleRestored != null){
            return ResponseEntity.ok().body(roleRestored);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
    }
}
