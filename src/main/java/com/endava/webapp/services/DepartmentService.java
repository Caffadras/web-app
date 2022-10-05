package com.endava.webapp.services;

import com.endava.webapp.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> findAll();
    Optional<Department> findById(Long id);
    Department save(Department department);
    Department update(Long id, Department newDepartment);
    void deleteById(Long id);
}
