package com.endava.webapp.services;

import com.endava.webapp.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {
    Page<Department> findAll(Pageable pageable);
    Optional<Department> findById(Long id);
    Department save(Department department);
    Department update(Long id, Department newDepartment);
    void deleteById(Long id);
}
