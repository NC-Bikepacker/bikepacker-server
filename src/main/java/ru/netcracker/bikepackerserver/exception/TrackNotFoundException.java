package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;

public class TrackNotFoundException extends BaseException{
    public TrackNotFoundException(Long id) {
        httpStatus = HttpStatus.NOT_FOUND;
        description = "Track with id " + id + " is not found.";
    }
}
