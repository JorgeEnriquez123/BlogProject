package com.jorge.blogproject.service.genericDAO;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    List<T> findAll();
    T save(T t);
    Optional<T> findById(Long id);

}

