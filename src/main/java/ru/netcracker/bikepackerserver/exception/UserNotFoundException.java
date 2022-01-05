package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException{

    public UserNotFoundException(Long id) {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "User with id " + id + " is not found.";
    }
}