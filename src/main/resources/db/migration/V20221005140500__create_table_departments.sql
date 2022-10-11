CREATE TABLE departments
(
    department_id   NUMBER(4) PRIMARY KEY,
    department_name VARCHAR2(30) NOT NULL,
    location_name   VARCHAR2(30) NOT NULL
);

CREATE SEQUENCE departments_seq  MINVALUE 1 MAXVALUE 9999 INCREMENT BY 1 START WITH 10;

COMMENT ON TABLE departments IS 'Departments table that shows details of departments where employees work.';
COMMENT ON COLUMN departments.department_id IS 'Primary key. Department id in the database';
COMMENT ON COLUMN departments.department_name IS 'Name of the department. A not null column.';
COMMENT ON COLUMN departments.location_name IS 'Name of the location where department is located. A not null column.';
