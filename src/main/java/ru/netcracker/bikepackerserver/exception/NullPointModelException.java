package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NullPointModelException extends BaseException {

    public NullPointModelException() {
        httpStatus = HttpStatus.NO_CONTENT;
        description = "PointModel is null.";
    }
}
