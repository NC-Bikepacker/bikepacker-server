package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullPointModelException extends BaseException {

    public NullPointModelException() {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "PointModel is null.";
    }
}
