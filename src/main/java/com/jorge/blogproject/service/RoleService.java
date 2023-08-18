package com.jorge.blogproject.service;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.repository.RoleRepository;
import com.jorge.blogproject.service.genericDAO.GenericDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements GenericDao<RoleEntity> {
    final private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }
    public Page<RoleEntity> findAllPaginable(Pageable pageable) {
        return roleRepository.findAll(pageable);
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

    public RoleEntity delete(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
        if(roleEntity != null){
            roleEntity.setEnabled(false);
            roleRepository.save(roleEntity);
            return roleEntity;
        }
        return roleEntity;
    }
    public RoleEntity restore(Long id){
        RoleEntity roleFound = roleRepository.findById(id).orElse(null);
        if(roleFound != null){
            roleFound.setEnabled(true);
            return roleRepository.save(roleFound);
        }
        return roleFound;
    }
}
