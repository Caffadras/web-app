package com.endava.webapp.services.jdbc;

import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.DepartmentRepository;
import com.endava.webapp.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> findAll() {
        try {
            return departmentRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Department> findById(Long id) {
        try {
            return departmentRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Department save(Department department) {
        try {
            return departmentRepository.save(department);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Department update(Long id, Department newDepartment) {
        try {
            if (departmentRepository.findById(id).isEmpty()){
                throw new NotFoundException("Department with id: " + id + " was not found.");
            }
            return departmentRepository.update(id, newDepartment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            departmentRepository.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
