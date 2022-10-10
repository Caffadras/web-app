package com.endava.webapp.repositories.jdbctemplate;

import com.endava.webapp.model.Department;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentRepositoryImplIT {

    @Autowired
    DepartmentRepositoryImpl departmentRepository;
    private static final Department department1 =
            new Department(1234L, "Test Department 1", "Test Location 1");

    private static final Department department2 =
            new Department(4321L, "Test Department 2", "Test Location 2");

    private static final Department department3 =
            new Department(4444L, "Test Department 3", "Test Location 3");

    @Test
    @Order(1)
    void shouldReturnEmptyOptional_WhenFindByNonExistingId() {
        assertTrue(departmentRepository.findById(department1.getDepartmentId()).isEmpty());
    }

    @Test
    @Order(2)
    void shouldReturnDepartment_WhenSaveCorrectDepartment() {
        assertEquals(departmentRepository.save(department1), department1);
    }

    //todo: add ID-based equals method to Department to pass the test
    @Test
    @Order(3)
    void shouldReturnCorrectOptional_WhenFindByExistingId() {
        assertEquals(departmentRepository.findById(department1.getDepartmentId()).get(), department1);
    }

    //todo: add ID-based equals method to Department to pass the test
    @Test
    @Order(4)
    void shouldReturnCorrectList_WhenCalledFindAll() {
        List<Department> expectedList = List.of(department1, department2);

        departmentRepository.save(department2);
        List<Department> actualList = departmentRepository.findAll();

        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    @Order(5)
    void shouldReturnNull_WhenUpdateNonExistingDepartment() {
        assertNull(departmentRepository.update(department3.getDepartmentId(), department2));
    }

    @Test
    @Order(6)
    void shouldReturnCorrectDepartment_WhenUpdateExistingDepartment() {
        assertEquals(departmentRepository.update(department2.getDepartmentId(), department3), department3);
    }

    @Test
    @Order(7)
    void shouldDeleteDepartment_WhenProvidedValidId() {
        departmentRepository.deleteById(department1.getDepartmentId());
        departmentRepository.deleteById(department3.getDepartmentId());
        assertTrue(departmentRepository.findById(department1.getDepartmentId()).isEmpty());
        assertTrue(departmentRepository.findById(department2.getDepartmentId()).isEmpty());
    }

}
