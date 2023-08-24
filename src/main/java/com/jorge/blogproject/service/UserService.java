package com.jorge.blogproject.service;

import com.jorge.blogproject.model.Request.LoginRequest;
import com.jorge.blogproject.model.Request.UserRequest;
import com.jorge.blogproject.model.Request.UserUpdateRequest;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.repository.RoleRepository;
import com.jorge.blogproject.repository.UserRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericDao<UserEntity> {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
    public Page<UserEntity> findAllPaginable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
    public UserEntity update(Long userId, UserUpdateRequest userUpdateRequest){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity != null){
            userEntity.setUsername(userUpdateRequest.username());
            userEntity.setEmail(userUpdateRequest.email());

            String hashedPassword = passwordEncoder.encode(userUpdateRequest.password());
            userEntity.setPassword(hashedPassword);
            return userRepository.save(userEntity);
        }
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public UserEntity assignRoleToUser(Long userid, Long roleid){
        UserEntity userFound = userRepository.findById(userid).orElse(null);
        if (userFound != null) {
            RoleEntity roleFound = roleRepository.findById(roleid).orElse(null);
            if(roleFound != null){
                userFound.getRoles().add(roleFound);
                return userRepository.save(userFound);
            }
            throw new EntityNotFoundException("Rol no encontrado");
        }
        throw new EntityNotFoundException("Usuario no encontrado");
    }

    public UserEntity removeRoleFromUser(Long userid, Long roleid){
        UserEntity userFound = userRepository.findById(userid).orElse(null);
        if (userFound != null) {
            RoleEntity roleFound = roleRepository.findById(roleid).orElse(null);
            if(roleFound != null){
                userFound.getRoles().remove(roleFound);
                return userRepository.save(userFound);
            }
            throw new EntityNotFoundException("Rol no encontrado");
        }
        throw new EntityNotFoundException("Usuario no encontrado");
    }

    public UserEntity delete(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if(userEntity != null){
            userEntity.setEnabled(false);
            userRepository.save(userEntity);
            return userEntity;
        }
        return userEntity;
    }

    public UserEntity restore(Long id){
        UserEntity userFound = userRepository.findById(id).orElse(null);
        if(userFound != null){
            userFound.setEnabled(true);
            return userRepository.save(userFound);
        }
        return userFound;
    }
}
