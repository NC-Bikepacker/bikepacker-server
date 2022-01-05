package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsersDeletingException extends BaseException{

    public UsersDeletingException() {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "No one user exists.";
    }
}