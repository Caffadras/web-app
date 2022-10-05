--------------------------------------------------------
--  Ref Constraints for Table EMPLOYEES
--------------------------------------------------------

ALTER TABLE employees
    ADD CONSTRAINT EMP_DEPT_FK FOREIGN KEY (department_id)
        REFERENCES departments (department_id);