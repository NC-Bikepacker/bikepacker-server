package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    protected HttpStatus httpStatus;
    protected String description;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }
}
