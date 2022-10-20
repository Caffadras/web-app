package com.endava.webapp.controllers;

import com.endava.webapp.DepartmentResourceAssembler;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.services.DepartmentService;
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

@WebMvcTest(controllers = DepartmentsController.class)
class DepartmentsControllerTest {
    static final Department department1 = new Department(
            1234L, "Test Department 1", "Test Location 1", new ArrayList<>());

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    DepartmentService departmentService;
    @MockBean
    DepartmentResourceAssembler resourceAssembler;
    @MockBean
    PagedResourcesAssembler<Department> pagedResourcesAssembler;

    @Test
    void contextLoads () {
    }

    @Test
    void shouldReturn200_whenGetAll() throws Exception{
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404_whenGetNonExistingDepartment() throws Exception {
        mockMvc.perform(get("/department/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn200_whenGetExistingDepartment() throws Exception{
        Optional<Department> departmentOptional = Optional.of(department1);
        doReturn(departmentOptional).when(departmentService).findById(eq(department1.getDepartmentId()));

        mockMvc.perform(get("/departments/"+department1.getDepartmentId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenGetDepartmentWrongIDFormat() throws Exception{
        mockMvc.perform(get("/departments/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenSaveValidDepartment() throws Exception{
        Department departmentDto = createValidDepartmentDto();

        mockMvc.perform(post("/departments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenSaveInvalidDepartment() throws Exception{
        Department departmentDto = createInvalidDepartmentDto();

        mockMvc.perform(post("/departments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn404_whenDeleteNonExistingDepartment() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(departmentService).deleteById(any());

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn400_whenDeleteDepartmentWithEmployees() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(departmentService).deleteById(any());

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenDeleteDepartmentWrongIDFormat() throws Exception{
        mockMvc.perform(delete("/departments/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    void shouldReturn200_whenDeleteValidDepartment() throws Exception{
        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    void shouldReturn404_whenUpdateNonExistingDepartment() throws Exception{
        Department departmentDto = createValidDepartmentDto();
        doThrow(NotFoundException.class).when(departmentService).update(any(),any());

        mockMvc.perform(put("/departments/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldReturn400_whenUpdateInvalidDepartment() throws Exception{
        Department departmentDto = createInvalidDepartmentDto();

        mockMvc.perform(put("/departments/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn400_whenUpdateDepartmentWrongIDFormat() throws Exception{
        mockMvc.perform(put("/departments/foo"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldReturn200_whenUpdateCorrectDepartment() throws Exception{
        Department departmentDto = createValidDepartmentDto();

        mockMvc.perform(put("/departments/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isOk());
    }


    private Department createValidDepartmentDto(){
         Department departmentDto = new Department();
         departmentDto.setDepartmentName("DepartmentDto");
         departmentDto.setLocationName("Test Location");
        return departmentDto;
    }

    private Department createInvalidDepartmentDto(){
        Department departmentDto = new Department();
        departmentDto.setDepartmentName(" ");
        departmentDto.setLocationName(" ");
        return departmentDto;
    }
}