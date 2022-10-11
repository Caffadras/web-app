package com.endava.webapp.repositories;

import com.endava.webapp.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {
    List<Department> findAll();

    Optional<Department> findById(Long id);

    Department save(Department department);

    Department update(Department department);

    void deleteById(Long id);
}
