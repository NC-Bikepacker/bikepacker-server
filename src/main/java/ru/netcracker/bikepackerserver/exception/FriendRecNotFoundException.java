package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FriendRecNotFoundException extends BaseException {

    public FriendRecNotFoundException() {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "User with id " + " is not found.";
    }
}
