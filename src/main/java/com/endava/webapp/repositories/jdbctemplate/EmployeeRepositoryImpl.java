package com.endava.webapp.repositories.jdbctemplate;

import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.EmployeeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> findAll() {
        String query = "SELECT * FROM employees";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Employee.class));
    }

    @Override
    public Optional<Employee> findById(Long id) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";
        try{
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Employee.class), id));
        }catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Employee save(Employee employee) {
        String query = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{employee.getEmployeeId(), employee.getFirstName(), employee.getFirstName(),
                employee.getEmail(), employee.getPhoneNumber(), employee.getSalary(), employee.getDepartmentId()};

        if (jdbcTemplate.update(query, params) > 0){
            return employee;
        }
        return null;
    }

    @Override
    public Employee update(Long id, Employee employee) {
        String query = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, " +
                "phone_number = ?, salary = ?, department_id = ? WHERE employee_id = ?";
        Object[] params = new Object[]{employee.getFirstName(), employee.getFirstName(),
                employee.getEmail(), employee.getPhoneNumber(), employee.getSalary(), employee.getDepartmentId(), id};

        if (jdbcTemplate.update(query, params) > 0){
            employee.setEmployeeId(id);
            return employee;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM employees WHERE employee_id = ?";
        jdbcTemplate.update(query, id);
    }

}
