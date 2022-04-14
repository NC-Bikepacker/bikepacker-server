package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorException extends BaseException {

    public ServerErrorException() {
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        description = "Server error.";
    }
}
