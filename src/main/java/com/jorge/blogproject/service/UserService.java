package com.jorge.blogproject.service;

import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.repository.UserRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericDao<UserEntity> {
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
