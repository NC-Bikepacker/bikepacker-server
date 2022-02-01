package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FriendAlreadyExistsException extends BaseException {

    public FriendAlreadyExistsException(String user, String friend) {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Users: " + user + "and " + friend + "are already friends.";
    }
}