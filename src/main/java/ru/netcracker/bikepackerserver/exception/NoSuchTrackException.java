package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchTrackException extends BaseException {

    public NoSuchTrackException() {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "There is no track with such id.";
    }
}