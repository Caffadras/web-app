package com.endava.webapp.services.hibernate;

import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.model.Department;
import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.DepartmentRepository;
import com.endava.webapp.repositories.EmployeeRepository;
import com.endava.webapp.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        for (Employee employee : employeeList) {
            employee.getAndSetDepartmentId();
        }
        return employeeList;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        employee.ifPresent(Employee::getAndSetDepartmentId);
        return employee;
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getDepartment() == null){
            findDepartmentForEmployee(employee);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, Employee newEmployee) {
        newEmployee.setEmployeeId(id);
        findDepartmentForEmployee(newEmployee);
        return employeeRepository.update(newEmployee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    private void findDepartmentForEmployee(Employee employee){
        Optional<Department> department = departmentRepository.findById(employee.getDepartmentId());
        if (department.isEmpty()){
            throw new NotFoundException("Employee references non existing Department with id: "
                    + employee.getDepartmentId());
        }
        employee.setDepartment(department.get());
    }
}
