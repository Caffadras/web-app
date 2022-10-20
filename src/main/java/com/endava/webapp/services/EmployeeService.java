package com.endava.webapp.services;

import com.endava.webapp.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {
    Page<Employee> findAll(Pageable pageable);
    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    Employee update(Long id, Employee newEmployee);
    void deleteById(Long id);
}
