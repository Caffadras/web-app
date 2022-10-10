package com.endava.webapp.repositories;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    T save(T object);
    T update(Long id, T object);
    void deleteById(Long id);
}
