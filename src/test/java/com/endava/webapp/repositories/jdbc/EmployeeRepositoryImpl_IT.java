package com.endava.webapp.repositories.jdbc;

import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.StatementGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRepositoryImpl_IT {

    @Autowired
    EmployeeRepositoryImpl employeeRepository;

    @Autowired
    DepartmentRepositoryImpl departmentRepository;

    @Autowired
    StatementGenerator statementGenerator;


    private static final Department departmentReference =
            new Department(1235L, "Test Department 1", "Test Location 1");
    private static final Employee employee1 =
            new Employee(9991L, "Test First Name 1", "Test Last Name 1",
                    "Test Email 1", "Test Phone Number 1", 1000, -999L);
    private static final Employee employee2 =
            new Employee(9992L, "Test First Name 2", "Test Last Name 2",
                    "Test Email 2", "Test Phone Number 2", 1000, departmentReference.getDepartmentId());
    private static final Employee employee3 =
            new Employee(9993L, "Test First Name 3", "Test Last Name 3",
                    "Test Email 3", "Test Phone Number 3", 1000, departmentReference.getDepartmentId());


    @BeforeEach
    void setUp() throws SQLException {
        statementGenerator.getConnection().setAutoCommit(false);
        departmentRepository.save(departmentReference);
    }

    @AfterEach
    void tearDown() throws SQLException {
        statementGenerator.getConnection().rollback();
    }

    @Test
    @Order(1)
    void shouldNotSaveEmployeeWithNonExistingForeignKey(){
        assertThrows(SQLException.class, () -> employeeRepository.save(employee1));
    }

    @Test
    @Order(2)
    void shouldReturnSavedObject() throws SQLException {
        employee1.setDepartmentId(departmentReference.getDepartmentId());

        Employee actualEmployee = employeeRepository.save(employee1);
        assertEquals(employee1, actualEmployee);
    }

    @Test
    void shouldNotSaveDuplicates() throws SQLException {
        employeeRepository.save(employee1);
        assertThrows(SQLException.class, () -> employeeRepository.save(employee1));
    }

    @Test
    void shouldFindByIdSavedEmployee() throws SQLException {
        employeeRepository.save(employee1);
        Long actualId = employeeRepository.findById(employee1.getEmployeeId()).get().getEmployeeId();
        assertEquals(employee1.getEmployeeId(), actualId);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindByNonExistingId() throws SQLException {
        assertTrue(employeeRepository.findById(employee1.getEmployeeId()).isEmpty());
    }

    @Test
    void shouldReturnList_whenCalledFindAll() throws SQLException {
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        List<Employee> expectedList = List.of(employee1, employee2);

        List<Employee> actualList = employeeRepository.findAll();
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    void shouldReturnNull_whenUpdateNonExistingId() throws SQLException {
        assertNull(employeeRepository.update(employee1.getEmployeeId(), employee1));
    }

    @Test
    void shouldReturnEmployee_whenUpdateCorrectId() throws SQLException {
        employeeRepository.save(employee1);

        Employee actualEmployee = employeeRepository.update(employee1.getEmployeeId(), employee3);

        assertEquals(employee3.getFirstName(), actualEmployee.getFirstName());
    }

    @Test
    void shouldNotChangeId_whenUpdated() throws SQLException{
        employeeRepository.save(employee2);

        Employee actualEmployee = employeeRepository.update(employee2.getEmployeeId(), employee3);

        assertEquals(employee3.getEmployeeId(), actualEmployee.getEmployeeId());
    }

    @Test
    void shouldNotThrowException_whenDeleteByNonExistingId() throws SQLException{
        assertDoesNotThrow(() -> employeeRepository.deleteById(employee1.getEmployeeId()));
    }

    @Test
    void shouldDeleteDepartment_whenDeleteByCorrectId() throws SQLException{
        employeeRepository.save(employee1);

        employeeRepository.deleteById(employee1.getEmployeeId());

        assertTrue(employeeRepository.findById(employee1.getEmployeeId()).isEmpty());
    }

}