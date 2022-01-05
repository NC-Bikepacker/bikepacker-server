package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException(String email) {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Email address " + email + " is already in use.";
    }
}