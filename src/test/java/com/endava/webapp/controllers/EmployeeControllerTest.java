package com.endava.webapp.controllers;

import com.endava.webapp.EmployeeResourceAssembler;
import com.endava.webapp.dto.EmployeeDto;
import com.endava.webapp.dto.EmployeeDtoConverter;
import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    private static final Department departmentReference =
            new Department(1235L, "Test Department 1", "Test Location 1", new ArrayList<>());
    private static final Employee employee1 =
            new Employee(9991L, "Test First Name 1", "Test Last Name 2",
                    "email@gmail.com", "37383467655", 1000, departmentReference);
    
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    EmployeeService employeeService;
    @MockBean
    EmployeeDtoConverter converter;
    @MockBean
    EmployeeResourceAssembler resourceAssembler;
    @MockBean
    PagedResourcesAssembler<Employee> pagedResourcesAssembler;

    @Test
    void contextLoads () {
    }

    @Test
    void shouldReturn200_whenGetAll() throws Exception{
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404_whenGetNonExistingEmployee() throws Exception {
        mockMvc.perform(get("/employees/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn400_whenGetEmployeeWrongIDFormat() throws Exception{
        mockMvc.perform(get("/employees/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenGetExistingEmployee() throws Exception{
        Optional<Employee> employeeOptional = Optional.of(employee1);
        doReturn(employeeOptional).when(employeeService).findById(eq(employee1.getEmployeeId()));

        mockMvc.perform(get("/employees/"+employee1.getEmployeeId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenSaveEmployeeWithNonExistingDepartment() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();
        doThrow(InvalidForeignKeyException.class).when(employeeService).save(any());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenSaveNotUniqueEmployee() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();
        doThrow(DataIntegrityViolationException.class).when(converter).fromEmployeeDto(any());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenSaveInvalidEmployee() throws Exception{
        EmployeeDto employeeDto = createInvalidEmployeeDto();

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenSaveValidEmployee() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404_whenDeleteNonExistingEmployee() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(employeeService).deleteById(any());

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn400_whenDeleteEmployeeWrongIDFormat() throws Exception{
        mockMvc.perform(delete("/employees/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenDeleteValidEmployee() throws Exception{
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    void shouldReturn404_whenUpdateNonExistingEmployee() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();
        doThrow(NotFoundException.class).when(employeeService).update(any(),any());

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn400_whenUpdateEmployeeWithNonExistingDepartment() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();
        doThrow(InvalidForeignKeyException.class).when(converter).fromEmployeeDto(any());

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenUpdateNotUniqueEmployee() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();
        doThrow(DataIntegrityViolationException.class).when(employeeService).update(any(), any());

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenUpdateInvalidEmployee() throws Exception{
        EmployeeDto employeeDto = createInvalidEmployeeDto();

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenUpdateEmployeeWrongIDFormat() throws Exception{
        mockMvc.perform(put("/employees/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenUpdateCorrectEmployee() throws Exception{
        EmployeeDto employeeDto = createValidEmployeeDto();

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk());
    }
    
    private EmployeeDto createValidEmployeeDto(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Test first name");
        employeeDto.setLastName("Test last name");
        employeeDto.setEmail("email@gmail.com");
        employeeDto.setPhoneNumber("37385761099");
        employeeDto.setSalary(1000);
        employeeDto.setDepartmentId(1L);

        return employeeDto;
    }

    private EmployeeDto createInvalidEmployeeDto(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName(" ");
        employeeDto.setLastName(" ");
        employeeDto.setEmail("email");
        employeeDto.setPhoneNumber("phone");
        employeeDto.setSalary(-1000);
        employeeDto.setDepartmentId(null);

        return employeeDto;
    }
}