package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends BaseException {

    public UsernameAlreadyExistsException(String username) {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Username " + username + " is already in use.";
    }
}