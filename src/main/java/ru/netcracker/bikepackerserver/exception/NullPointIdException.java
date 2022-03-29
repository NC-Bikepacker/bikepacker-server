package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullPointIdException extends BaseException {

    public NullPointIdException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Point id is null";
    }
}
