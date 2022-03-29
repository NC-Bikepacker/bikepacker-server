package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullTrackIdException extends BaseException {

    public NullTrackIdException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Track id is null";
    }
}
