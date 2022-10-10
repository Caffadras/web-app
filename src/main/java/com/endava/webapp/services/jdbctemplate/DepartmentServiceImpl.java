package com.endava.webapp.services.jdbctemplate;

import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.DepartmentRepository;
import com.endava.webapp.services.DepartmentService;
import org.springframework.stereotype.Service;

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
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department update(Long id, Department newDepartment) {
        return departmentRepository.update(id, newDepartment);
    }

    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
