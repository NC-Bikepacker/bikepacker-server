package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongCoordinatesException extends BaseException {

    public WrongCoordinatesException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Perhaps the coordinates in the request are incorrect.";
    }
}
