package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NullPointEntityException extends BaseException {

    public NullPointEntityException() {
        httpStatus = HttpStatus.NO_CONTENT;
        description = "PointEntity is null.";
    }
}
