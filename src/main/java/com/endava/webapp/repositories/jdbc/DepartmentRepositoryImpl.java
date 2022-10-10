package com.endava.webapp.repositories.jdbc;

import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.DepartmentRepository;
import com.endava.webapp.repositories.StatementGenerator;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    private final StatementGenerator statementGenerator;

    public DepartmentRepositoryImpl(StatementGenerator statementGenerator) {
        this.statementGenerator = statementGenerator;
    }

    @Override
    public List<Department> findAll() throws SQLException{
        List <Department> departments = new ArrayList<>();
        String query = "SELECT * FROM departments";
        try (ResultSet resultSet = statementGenerator.createPrepared(query).executeQuery())
        {
            while (resultSet.next()) {
                departments.add(DepartmentRowMapper.mapRow(resultSet));
            }
        }
        return departments;
    }

    @Override
    public Optional<Department> findById(Long id) throws SQLException{
        String query = "SELECT * FROM departments WHERE department_id = ?";

        try (ResultSet resultSet = statementGenerator.createPrepared(query, id).executeQuery()) {
            if (resultSet.next()){
                return Optional.of(DepartmentRowMapper.mapRow(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public Department save(Department department) throws SQLException{
        String query = "INSERT INTO departments VALUES (?, ?, ?)";
        Object[] params = new Object[]{department.getDepartmentId(), department.getDepartmentName(), department.getLocation()};

        if (statementGenerator.createPrepared(query, params).executeUpdate() > 0){
            return department;
        }
        return null;
    }

    @Override
    public Department update(Long id, Department department) throws SQLException{
        String query = "UPDATE departments SET department_name = ?, location_name = ? WHERE department_id = ?";
        Object[] params = new Object[]{department.getDepartmentName(), department.getLocation(), id};

        if (statementGenerator.createPrepared(query, params).executeUpdate() > 0){
            department.setDepartmentId(id);
            return department;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        String query = "DELETE FROM departments WHERE department_id = ?";
        statementGenerator.createPrepared(query, id).execute();
    }

    private static final class DepartmentRowMapper {
        public static Department mapRow(ResultSet resultSet) throws SQLException{
            Department department = new Department();
            department.setDepartmentId(resultSet.getLong("department_id"));
            department.setDepartmentName(resultSet.getString("department_name"));
            department.setLocation(resultSet.getString("location_name"));

            return department;
        }
    }
}
