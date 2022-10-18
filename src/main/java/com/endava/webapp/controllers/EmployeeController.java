package com.endava.webapp.controllers;

import com.endava.webapp.dto.EmployeeDto;
import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.services.DepartmentService;
import com.endava.webapp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @GetMapping
    public List<EmployeeDto> findAll(){
        return employeeService.findAll()
                .stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmployeeDto findById(@PathVariable Long id){
        return new EmployeeDto(employeeService.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee with id: " + id + " was not found.")));
    }

    @PostMapping
    public EmployeeDto save(@RequestBody EmployeeDto employeeDto){
        return new EmployeeDto(employeeService.save(convertFromEmployeeDto(employeeDto)));
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto){
        return new EmployeeDto(employeeService.update(id, convertFromEmployeeDto(employeeDto)));
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
