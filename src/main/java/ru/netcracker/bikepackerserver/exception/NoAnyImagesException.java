package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoAnyImagesException extends BaseException {

    public NoAnyImagesException() {
        httpStatus = HttpStatus.NO_CONTENT;
        description = "There are no any images.";
    }
}