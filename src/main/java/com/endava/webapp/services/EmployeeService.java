package com.endava.webapp.services;

import com.endava.webapp.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    Employee update(Long id, Employee newEmployee);
    void deleteById(Long id);
}
