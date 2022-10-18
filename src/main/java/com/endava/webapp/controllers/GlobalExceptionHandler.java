package com.endava.webapp.controllers;

import com.endava.webapp.exceptions.ErrorResponse;
import com.endava.webapp.exceptions.InvalidForeignKeyException;
import com.endava.webapp.exceptions.NotFoundException;
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



    //Exposing server error messages like this is not a great idea. Added just for the convenience.
//    @ResponseBody
//    @ExceptionHandler(SQLException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String sqlExceptionHandler(SQLException e){
//        return e.getMessage();
//    }
}
