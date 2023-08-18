package com.jorge.blogproject.service;

import com.jorge.blogproject.model.Request.LoginRequest;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.repository.UserRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericDao<UserEntity> {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    // * SIMULAR LOGUEO CON DATOS DE LA BD - NO LOGIN REAL
    public UserEntity login(LoginRequest loginRequest){
        UserEntity userFound = userRepository.findByUsername(loginRequest.username());
        if(userFound == null || !passwordEncoder.matches(loginRequest.password(), userFound.getPassword())){
            // User not found or User's password is incorrect
            return null;
        }
        return userFound;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username);
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
