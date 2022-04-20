package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;

public class GpxDestroyedException extends BaseException{
    public GpxDestroyedException(Long trackId) {
        httpStatus = HttpStatus.NO_CONTENT;
        description = "Track gpx with id " + trackId+ " is destroyed.";
    }
}
