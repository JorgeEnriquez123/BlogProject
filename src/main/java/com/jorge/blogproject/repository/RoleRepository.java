package com.jorge.blogproject.repository;

import com.jorge.blogproject.model.Enums.EnumRole;
import com.jorge.blogproject.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName (EnumRole enumRole);
}
