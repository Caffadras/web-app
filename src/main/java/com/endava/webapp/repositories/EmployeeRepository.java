package com.endava.webapp.repositories;

import com.endava.webapp.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    Employee update(Employee employee);

    void deleteById(Long id);
}
