package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoAnyPointException extends BaseException {

    public NoAnyPointException() {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "Point not found.";
    }
}
