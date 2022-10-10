package com.endava.webapp.repositories.jdbctemplate;

import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.DepartmentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Department> findAll() {
        String query = "SELECT * FROM departments";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Department.class));
    }

    @Override
    public Optional<Department> findById(Long id) {
        String query = "SELECT * FROM departments WHERE department_id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Department.class), id));
        } catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Department save(Department department) {
        String query = "INSERT INTO departments VALUES (?, ?, ?)";
        if (jdbcTemplate.update(query, department.getDepartmentId(), department.getDepartmentName(), department.getLocation()) > 0){
            return department;
        }

        return null;
    }

    @Override
    public Department update(Long id, Department department) {
        String query = "UPDATE departments SET department_name = ?, location_name = ? WHERE department_id = ?";
        if (jdbcTemplate.update(query, department.getDepartmentName(), department.getLocation(), id) > 0){
            department.setDepartmentId(id);
            return department;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM departments WHERE department_id = ?";
        jdbcTemplate.update(query, id);
    }

}
