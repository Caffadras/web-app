package com.endava.webapp.controllers;

import com.endava.webapp.EmployeeResourceAssembler;
import com.endava.webapp.dto.EmployeeDto;
import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.services.DepartmentService;
import com.endava.webapp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final EmployeeResourceAssembler employeeResourceAssembler;
    private final PagedResourcesAssembler<Employee> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<EmployeeDto>> findAll(@PageableDefault Pageable pageable){
        Page<Employee> all = employeeService.findAll(pageable);
        return pagedResourcesAssembler.toModel(all, employeeResourceAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<EmployeeDto> findById(@PathVariable Long id){
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee with id: " + id + " was not found."));

        return employeeResourceAssembler.toModel(employee);
    }

    @PostMapping
    public EntityModel<EmployeeDto> save(@RequestBody EmployeeDto employeeDto){
        return employeeResourceAssembler.toModel(
                employeeService.save(convertFromEmployeeDto(employeeDto)));
    }

    @PutMapping("/{id}")
    public EntityModel<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto){
        return employeeResourceAssembler.toModel(
                employeeService.update(id, convertFromEmployeeDto(employeeDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        employeeService.deleteById(id);
    }


    private Employee convertFromEmployeeDto(EmployeeDto employeeDto){
        Department department = departmentService.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new InvalidForeignKeyException(
                        "Employee has non existing department with id: " + employeeDto.getDepartmentId()));

        return employeeDto.toEmployee(department);
    }
}
