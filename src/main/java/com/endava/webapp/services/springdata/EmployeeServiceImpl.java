package com.endava.webapp.services.springdata;

import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.springdata.EmployeeRepository;
import com.endava.webapp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Long id, Employee newEmployee) {
        newEmployee.setEmployeeId(id);
        if (employeeRepository.findById(id).isPresent()){
            return employeeRepository.save(newEmployee);
        }
        throw new NotFoundException("Employee with id: " + id + " was not found.");
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}