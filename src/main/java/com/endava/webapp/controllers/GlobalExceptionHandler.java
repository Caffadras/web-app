package com.endava.webapp.controllers;

import com.endava.webapp.exceptions.DepartmentsConstraintViolationException;
import com.endava.webapp.exceptions.ErrorResponse;
import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.exceptions.NotFoundException;
import com.endava.webapp.exceptions.UniqueConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String OBJECT_NOT_FOUND = "Object not found";
    public static final String INVALID_FOREIGN_KEY = "Invalid foreign key";
    public static final String DEPARTMENTS_CONSTRAINT_VIOLATION = "Departments constraint violation";
    public static final String UNIQUE_CONSTRAINT_VIOLATION = "Unique constraint violation";

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> notFoundHandler(HttpServletRequest request, NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), OBJECT_NOT_FOUND,
                        e.getMessage(), request.getServletPath()));
    }

    @ResponseBody
    @ExceptionHandler(InvalidForeignKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> invalidForeignKeyHandler(HttpServletRequest request, InvalidForeignKeyException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_FOREIGN_KEY,
                        e.getMessage(), request.getServletPath()));
    }

    @ResponseBody
    @ExceptionHandler(DepartmentsConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> departmentsConstraintViolationHandler(HttpServletRequest request,
                                                                               DepartmentsConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), DEPARTMENTS_CONSTRAINT_VIOLATION,
                        e.getMessage(), request.getServletPath()));
    }

    @ResponseBody
    @ExceptionHandler(UniqueConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> uniqueConstraintViolationHandler(HttpServletRequest request,
                                                                               UniqueConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), UNIQUE_CONSTRAINT_VIOLATION,
                        e.getMessage(), request.getServletPath()));
    }

}
