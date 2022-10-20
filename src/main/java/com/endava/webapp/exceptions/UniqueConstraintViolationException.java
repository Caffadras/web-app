package com.endava.webapp.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class UniqueConstraintViolationException extends DataIntegrityViolationException {
    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
