package com.example.app.dbdesignbackend.exception;

public class UnableConnectToDatabaseException extends RuntimeException{
    public UnableConnectToDatabaseException(String message) {
        super(message);
    }
}
