package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FriendRecNotFoundException extends BaseException {

    public FriendRecNotFoundException(Long userId, Long friendId) {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "Friends with id " + " is not found.";
    }
}
