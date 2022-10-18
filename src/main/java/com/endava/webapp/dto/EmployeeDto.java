package com.endava.webapp.dto;

import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class EmployeeDto implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer salary;
    private Long departmentId;

    public EmployeeDto(Employee employee) {
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.phoneNumber = employee.getPhoneNumber();
        this.salary = employee.getSalary();
        this.departmentId = employee.getDepartment().getDepartmentId();
    }

    public Employee toEmployee(Department department){
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setSalary(salary);
        employee.setDepartment(department);
        return employee;
    }
}
