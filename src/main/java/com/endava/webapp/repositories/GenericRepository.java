package com.endava.webapp.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    List<T> findAll() throws SQLException;
    Optional<T> findById(Long id) throws SQLException;
    T save(T object) throws SQLException;
    T update(Long id, T object) throws SQLException;
    void deleteById(Long id) throws SQLException;
}
