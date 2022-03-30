package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchTrackException extends BaseException {

    public NoSuchTrackException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "There is no track with such id.";
    }
}