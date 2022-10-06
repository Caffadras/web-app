package com.endava.webapp.services.jdbc;

import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.EmployeeRepository;
import com.endava.webapp.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        try {
            return employeeRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try {
            return employeeRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee save(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee update(Long id, Employee newEmployee) {
        try {
            if (employeeRepository.findById(id).isEmpty()){
                throw new NotFoundException("Employee with id: " + id + " was not found.");
            }
            return employeeRepository.update(id, newEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
