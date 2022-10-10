package com.endava.webapp.repositories.jdbctemplate;

import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.EmployeeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        return jdbcTemplate.query(query, new EmployeeRowMapper());
    }

    @Override
    public Optional<Employee> findById(Long id) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, new EmployeeRowMapper(), id));
    }

    @Override
    public Employee save(Employee employee) {
        String query = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{employee.getId(), employee.getFirstName(), employee.getFirstName(),
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
            employee.setId(id);
            return employee;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM employees WHERE employee_id = ?";
        jdbcTemplate.update(query, id);
    }

    public static class EmployeeRowMapper implements RowMapper<Employee>{
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong("employee_id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setEmail(rs.getString("email"));
            employee.setPhoneNumber(rs.getString("phone_number"));
            employee.setSalary(rs.getInt("salary"));
            employee.setDepartmentId(rs.getLong("department_id"));
            return employee;
        }
    }
}
