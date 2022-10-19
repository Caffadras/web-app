package com.endava.webapp.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class DepartmentsConstraintViolationException extends DataIntegrityViolationException {
    public DepartmentsConstraintViolationException(String msg) {
        super(msg);
    }
}
