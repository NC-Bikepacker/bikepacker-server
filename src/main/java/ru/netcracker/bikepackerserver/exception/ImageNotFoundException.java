package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends BaseException{

    public ImageNotFoundException(Long id) {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "Image with id " + id + " is not found.";
    }

}
