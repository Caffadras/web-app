package com.endava.webapp;

import com.endava.webapp.controllers.DepartmentsController;
import com.endava.webapp.model.Department;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentResourceAssembler implements RepresentationModelAssembler<Department, EntityModel<Department>> {
    @Override
    public EntityModel<Department> toModel(Department department) {
        return EntityModel.of(department,
                linkTo(methodOn(DepartmentsController.class).findById(department.getDepartmentId())).withSelfRel(),
                linkTo(methodOn(DepartmentsController.class).findAll(null)).withRel("all_departments")
                );
    }
}
