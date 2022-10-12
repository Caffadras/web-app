package com.endava.webapp.repositories.jdbctemplate;

import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRepositoryImplTest {

    @Autowired
    EmployeeRepositoryImpl employeeRepository;

    @Autowired
    DepartmentRepositoryImpl departmentRepository;


    private static final Department departmentReference =
            new Department(1235L, "Test Department 1", "Test Location 1");
    private static final Employee employee1 =
            new Employee(9991L, "Test First Name 1", "Test Last Name 2",
                    "Test Email 1", "Test Phone Number 1", 1000, -999L);
    private static final Employee employee2 =
            new Employee(9992L, "Test First Name 2", "Test Last Name 2",
                    "Test Email 2", "Test Phone Number 2", 1000, departmentReference.getDepartmentId());
    private static final Employee employee3 =
            new Employee(9993L, "Test First Name 3", "Test Last Name 3",
                    "Test Email 3", "Test Phone Number 3", 1000, departmentReference.getDepartmentId());


    @Test
    @Order(1)
    void shouldReturnEmptyOptional_WhenFindByNonExistingId() {
        assertTrue(employeeRepository.findById(employee1.getEmployeeId()).isEmpty());
    }

    @Test
    @Order(2)
    void shouldNOTSave_WhenReferencesNonExistingDepartmentId() {
        assertThrows(DataAccessException.class, () -> employeeRepository.save(employee1));
    }

    @Test
    @Order(3)
    void shouldReturnEmployee_WhenSaveCorrectEmployee() {
        employee1.setDepartmentId(departmentReference.getDepartmentId());
        departmentRepository.save(departmentReference);
        assertEquals(employeeRepository.save(employee1), employee1);
    }

    @Test
    @Order(4)
    void shouldReturnCorrectOptional_WhenFindByExistingId() {
        assertEquals(employeeRepository.findById(employee1.getEmployeeId()).get(), employee1);
    }

    @Test
    @Order(5)
    void shouldReturnCorrectList_WhenCalledFindAll() {
        List<Employee> expectedList = List.of(employee1, employee2);

        employeeRepository.save(employee2);
        List<Employee> actualList = employeeRepository.findAll();

        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    @Order(6)
    void shouldReturnNull_WhenUpdateNonExistingEmployee() {
        assertNull(employeeRepository.update(employee3.getEmployeeId(), employee2));
    }

    @Test
    @Order(7)
    void shouldReturnCorrectEmployee_WhenUpdateExistingEmployee() {
        assertEquals(employeeRepository.update(employee2.getEmployeeId(), employee3), employee3);
    }

    @Test
    @Order(8)
    void shouldDeleteEmployee_WhenProvidedValidId() {
        employeeRepository.deleteById(employee1.getEmployeeId());
        employeeRepository.deleteById(employee3.getEmployeeId());
        assertTrue(employeeRepository.findById(employee1.getEmployeeId()).isEmpty());
        assertTrue(employeeRepository.findById(employee2.getEmployeeId()).isEmpty());

        departmentRepository.deleteById(departmentReference.getDepartmentId());
    }
}