package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullTextException extends BaseException {

    public NullTextException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Text is null.";
    }
}
