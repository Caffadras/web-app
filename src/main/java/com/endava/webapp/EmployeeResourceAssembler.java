package com.endava.webapp;

import com.endava.webapp.controllers.DepartmentsController;
import com.endava.webapp.controllers.EmployeeController;
import com.endava.webapp.dto.EmployeeDto;
import com.endava.webapp.dto.EmployeeDtoConverter;
import com.endava.webapp.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class EmployeeResourceAssembler implements RepresentationModelAssembler<Employee, EntityModel<EmployeeDto>> {

    private final EmployeeDtoConverter employeeDtoConverter;
    @Override
    public EntityModel<EmployeeDto> toModel(Employee employee) {
        return EntityModel.of(employeeDtoConverter.toEmployeeDto(employee),
                linkTo(methodOn(EmployeeController.class).findById(employee.getEmployeeId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).findAll(null)).withRel("all_employees"),
                linkTo(methodOn(DepartmentsController.class)
                        .findById(employee.getDepartment().getDepartmentId())).withRel("employee_department"));
    }
}
