package com.endava.webapp.exceptions;

public class InvalidForeignKeyException extends RuntimeException{
    public InvalidForeignKeyException(String message) {
        super(message);
    }
}
