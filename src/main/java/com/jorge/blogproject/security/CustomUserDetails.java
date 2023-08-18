package com.jorge.blogproject.security;

import com.jorge.blogproject.model.RoleEntity;
import com.jorge.blogproject.model.UserEntity;
import com.jorge.blogproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        System.out.println(user);
        // Si el usuario está deshabilitado, no tiene ninguna autoridad
        List<GrantedAuthority> authorities = null;
        // Si el usuario está habilitado, tiene las autoridades de su roles
        if (user.isEnabled()) {
            authorities = user.getRoles().stream()
                    .filter(RoleEntity::getEnabled) //Filtrar los roles habilitados
                    .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName().name()))
                    .collect(Collectors.toList());
        }
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
