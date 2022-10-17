package com.endava.webapp.repositories.jdbc;

import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.StatementGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DepartmentRepositoryImpl_IT {

    @Autowired
    DepartmentRepositoryImpl departmentRepository;

    @Autowired
    StatementGenerator statementGenerator;

    private static final Department department1 =
            new Department(1234L, "Test Department 1", "Test Location 1");

    private static final Department department2 =
            new Department(4321L, "Test Department 2", "Test Location 2");

    private static final Department department3 =
            new Department(4444L, "Test Department 3", "Test Location 3");

    @BeforeEach
    void setUp() throws SQLException {
        statementGenerator.getConnection().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        statementGenerator.getConnection().rollback();
    }

    @Test
    void shouldReturnSavedObject() throws SQLException {
        Department actualDepartment = departmentRepository.save(department1);
        assertEquals(department1, actualDepartment);
    }

    @Test
    void shouldNotSaveDuplicates() throws SQLException {
        departmentRepository.save(department1);
        assertThrows(SQLException.class, () -> departmentRepository.save(department1));
    }

    @Test
    void shouldFindByIdSavedDepartment() throws SQLException {
        departmentRepository.save(department1);
        Long actualId = departmentRepository.findById(department1.getDepartmentId()).get().getDepartmentId();
        assertEquals(department1.getDepartmentId(), actualId);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindByNonExistingId() throws SQLException {
        assertTrue(departmentRepository.findById(department1.getDepartmentId()).isEmpty());
    }

    @Test
    void shouldReturnList_whenCalledFindAll() throws SQLException {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        List<Department> expectedList = List.of(department1, department2);

        List<Department> actualList = departmentRepository.findAll();
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    void shouldReturnNull_whenUpdateNonExistingId() throws SQLException {
        assertNull(departmentRepository.update(department1.getDepartmentId(), department1));
    }

    @Test
    void shouldReturnDepartment_whenUpdateCorrectId() throws SQLException {
        departmentRepository.save(department1);

        Department actualDepartment = departmentRepository.update(department1.getDepartmentId(), department3);

        assertEquals(department3.getDepartmentName(), actualDepartment.getDepartmentName());
    }

    @Test
    void shouldNotChangeId_whenUpdated() throws SQLException{
        departmentRepository.save(department2);

        Department actualDepartment = departmentRepository.update(department2.getDepartmentId(), department3);

        assertEquals(department3.getDepartmentId(), actualDepartment.getDepartmentId());
    }

    @Test
    void shouldNotThrowException_whenDeleteByNonExistingId() throws SQLException{
        assertDoesNotThrow(() -> departmentRepository.deleteById(department1.getDepartmentId()));
    }

    @Test
    void shouldDeleteDepartment_whenDeleteByCorrectId() throws SQLException{
        departmentRepository.save(department1);

        departmentRepository.deleteById(department1.getDepartmentId());

        assertTrue(departmentRepository.findById(department1.getDepartmentId()).isEmpty());
    }

}
