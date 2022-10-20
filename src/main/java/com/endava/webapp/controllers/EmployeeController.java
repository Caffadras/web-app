package com.endava.webapp.controllers;

import com.endava.webapp.EmployeeResourceAssembler;
import com.endava.webapp.dto.EmployeeDto;
import com.endava.webapp.dto.EmployeeDtoConverter;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.exceptions.UniqueConstraintViolationException;
import com.endava.webapp.model.Employee;
import com.endava.webapp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler employeeResourceAssembler;
    private final PagedResourcesAssembler<Employee> pagedResourcesAssembler;
    private final EmployeeDtoConverter converter;

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
    public EntityModel<EmployeeDto> save(@Valid @RequestBody EmployeeDto employeeDto){
        try{
            return employeeResourceAssembler.toModel(
                    employeeService.save(converter.fromEmployeeDto(employeeDto)));
        }catch (DataIntegrityViolationException e){
            throw new UniqueConstraintViolationException("Employee email and phone must be unique");
        }
    }

    @PutMapping("/{id}")
    public EntityModel<EmployeeDto> update(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto){
        try{
            return employeeResourceAssembler.toModel(
                    employeeService.update(id, converter.fromEmployeeDto(employeeDto)));
        }catch (DataIntegrityViolationException e){
            throw new UniqueConstraintViolationException("Employee email and phone must be unique");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        try {
            employeeService.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Employee with id: " + id + " was not found.");
        }
    }

}
