package com.endava.webapp.repositories.jdbc;

import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.EmployeeRepository;
import com.endava.webapp.repositories.StatementGenerator;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final StatementGenerator statementGenerator;

    public EmployeeRepositoryImpl(StatementGenerator statementGenerator) {
        this.statementGenerator = statementGenerator;
    }

    @Override
    public List<Employee> findAll() throws SQLException{
        List < Employee > employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (ResultSet resultSet = statementGenerator.createPrepared(query).executeQuery())
        {
            while (resultSet.next()) {
                employees.add(EmployeeRowMapper.mapRow(resultSet));
            }
        }
        return employees;
    }

    @Override
    public Optional<Employee> findById(Long id) throws SQLException{
        String query = "SELECT * FROM employees WHERE employee_id = ?";

        try (ResultSet resultSet = statementGenerator.createPrepared(query, id).executeQuery()) {
            if (resultSet.next()){
                return Optional.of(EmployeeRowMapper.mapRow(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public Employee save(Employee employee) throws SQLException{
        String query = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{employee.getId(), employee.getFirstName(), employee.getLastMame(),
                employee.getEmail(), employee.getPhoneNumber(), employee.getSalary(), employee.getDepartmentId()};

        if(statementGenerator.createPrepared(query, params).executeUpdate() > 0){
            return employee;
        }
        return null;
    }

    @Override
    public Employee update(Long id, Employee employee) throws SQLException{
        String query = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, " +
                "phone_number = ?, salary = ?, department_id = ? WHERE employee_id = ?";
        Object[] params = new Object[]{employee.getFirstName(), employee.getLastMame(),
                employee.getEmail(), employee.getPhoneNumber(), employee.getSalary(), employee.getDepartmentId(), id};

        if(statementGenerator.createPrepared(query, params).executeUpdate() > 0) {
            employee.setId(id);
            return employee;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        String query = "DELETE FROM employees WHERE employee_id = ?";
        statementGenerator.createPrepared(query, id).execute();
    }

    private static final class EmployeeRowMapper {
        public static Employee mapRow(ResultSet resultSet) throws SQLException{
            Employee employee = new Employee();
            employee.setId(resultSet.getLong("employee_id"));
            employee.setFirstName(resultSet.getString("first_name"));
            employee.setLastMame(resultSet.getString("last_name"));
            employee.setEmail(resultSet.getString("email"));
            employee.setPhoneNumber(resultSet.getString("phone_number"));
            employee.setSalary(resultSet.getInt("salary"));
            employee.setDepartmentId(resultSet.getLong("department_id"));
            return employee;
        }
    }
}
