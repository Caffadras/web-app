package com.endava.webapp.repositories.hibernate;

import com.endava.webapp.model.Department;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentRepositoryImplTest {

    @Autowired
    DepartmentRepositoryImpl departmentRepository;

    private static final Department department1 =
            new Department(1234L, "Test Department 1", "Test Location 1", new ArrayList<>());

    private static final Department department2 =
            new Department(4321L, "Test Department 2", "Test Location 2", new ArrayList<>());

    private static final Department department3 =
            new Department(4444L, "Test Department 3", "Test Location 3", new ArrayList<>());

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
        assertTrue(expectedList.containsAll(actualList));
    }

    @Test
    @Order(5)
    void shouldThrowException_WhenUpdateNonExistingDepartment() {
        assertThrows( RuntimeException.class, () -> departmentRepository.update(department3));
    }

    @Test
    @Order(6)
    void shouldReturnCorrectDepartment_WhenUpdateExistingDepartment() {
        String expectedName = "Expected Department Name";
        department2.setDepartmentName(expectedName);
        departmentRepository.update(department2);

        assertEquals(expectedName, department2.getDepartmentName());
    }

    @Test
    @Order(7)
    void shouldNOTChangeId_WhenUpdated() {
        Long expectedId = department2.getDepartmentId();
        departmentRepository.update(department2);

        assertEquals(expectedId, department2.getDepartmentId());
    }


    @Test
    @Order(8)
    void shouldDeleteDepartment_WhenProvidedValidId() {
        departmentRepository.deleteById(department1.getDepartmentId());
        departmentRepository.deleteById(department2.getDepartmentId());
        assertTrue(departmentRepository.findById(department1.getDepartmentId()).isEmpty());
        assertTrue(departmentRepository.findById(department2.getDepartmentId()).isEmpty());
    }

}
