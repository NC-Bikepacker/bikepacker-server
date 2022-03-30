package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoAnyImagesException extends BaseException {

    public NoAnyImagesException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "There are no any images.";
    }
}