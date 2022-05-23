package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoFileException extends BaseException{

    public NoFileException() {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "There is no track with such id.";
    }
}
