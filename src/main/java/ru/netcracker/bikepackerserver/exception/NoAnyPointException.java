package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoAnyPointException extends BaseException {

    public NoAnyPointException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Point not found.";
    }
}
