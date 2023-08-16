package com.jorge.blogproject.service;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.repository.RoleRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService implements GenericDao<RoleEntity> {
    final private RoleRepository roleRepository;

    public RolService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity save(RoleEntity RoleEntity) {
        return roleRepository.save(RoleEntity);
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return roleRepository.findById(id);
    }

    public RoleEntity findByName(EnumRole enumRole){
        return roleRepository.findByName(enumRole);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
