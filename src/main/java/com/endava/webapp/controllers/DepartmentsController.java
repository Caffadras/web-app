package com.endava.webapp.controllers;

import com.endava.webapp.DepartmentResourceAssembler;
import com.endava.webapp.exceptions.DepartmentsConstraintViolationException;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentsController {
    private final DepartmentService departmentService;
    private final DepartmentResourceAssembler resourceAssembler;
    private final PagedResourcesAssembler<Department> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<Department>> findAll(@PageableDefault Pageable pageable){
        Page<Department> all = departmentService.findAll(pageable);
        return pagedResourcesAssembler.toModel(all, resourceAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<Department> findById(@PathVariable Long id){
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new NotFoundException("Department with id: " + id + " was not found."));
        return resourceAssembler.toModel(department);
    }

    @PostMapping
    public EntityModel<Department> save(@Valid  @RequestBody Department department){
        return resourceAssembler.toModel(departmentService.save(department));
    }

    @PutMapping("/{id}")
    public EntityModel<Department> update(@PathVariable Long id, @Valid @RequestBody Department department){
        return resourceAssembler.toModel(departmentService.update(id, department));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        try {
            departmentService.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DepartmentsConstraintViolationException("Cannot delete department while it still has employees");
        }
    }
}
