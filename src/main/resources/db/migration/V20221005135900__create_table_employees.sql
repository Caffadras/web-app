CREATE TABLE employees
(
    employee_id    NUMBER(6) PRIMARY KEY,
    first_name     VARCHAR2(20)        NOT NULL,
    last_name      VARCHAR2(25)        NOT NULL,
    email          VARCHAR2(25) UNIQUE NOT NULL,
    phone_number   VARCHAR2(20) UNIQUE NOT NULL,
    salary         NUMBER(8, 2),
    department_id  NUMBER(4)           NOT NULL,
    CONSTRAINT emp_salary_min CHECK (salary >= 1)
);

COMMENT ON TABLE employees IS 'Employees table. References with departments.';
COMMENT ON COLUMN employees.employee_id IS 'Primary key of employees table.';
COMMENT ON COLUMN employees.first_name IS 'First name of the employee. A not null column.';
COMMENT ON COLUMN employees.last_name IS 'Last name of the employee. A not null column.';
COMMENT ON COLUMN employees.email IS 'Email of the employee. A not null, unique column.';
COMMENT ON COLUMN employees.phone_number IS 'Phone number of the employee; includes country code and area code.
A not null, unique column.';
COMMENT ON COLUMN employees.salary IS 'Monthly salary of the employee. Must be greater or equal
than 1 (enforced by constraint emp_salary_min)';
COMMENT ON COLUMN employees.department_id IS 'Department id where employee works; foreign key to department_id
column of the departments table';
