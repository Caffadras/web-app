package com.endava.webapp.dto;

import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto implements Serializable {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String phoneNumber;
    @Min(1)
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
