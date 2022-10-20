package com.endava.webapp.dto;

import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeDtoConverter {

    private final DepartmentService departmentService;

    public Employee fromEmployeeDto(EmployeeDto employeeDto){
        Department department = departmentService.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new InvalidForeignKeyException(
                        "Employee has non existing department with id: " + employeeDto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setSalary(employeeDto.getSalary());
        employee.setDepartment(department);
        return employee;
    }

    public EmployeeDto toEmployeeDto(Employee employee){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setSalary(employee.getSalary());
        employeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
        return employeeDto;
    }
}
