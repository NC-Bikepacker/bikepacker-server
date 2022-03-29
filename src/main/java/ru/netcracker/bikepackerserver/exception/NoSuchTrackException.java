package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoSuchTrackException extends BaseException {

    public NoSuchTrackException() {
        httpStatus = HttpStatus.NO_CONTENT;
        description = "There is no track with such id.";
    }
}
