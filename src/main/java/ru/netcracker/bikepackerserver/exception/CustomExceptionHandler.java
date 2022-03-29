package ru.netcracker.bikepackerserver.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = "ru.netcracker.bikepackerserver.controller")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseException> handleException(BaseException ex) {
        return new ResponseEntity<BaseException>(ex, ex.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Throwable> handleException(Throwable ex) {
        LoggerFactory.getLogger(CustomExceptionHandler.class).error("Error response", ex);
        return ResponseEntity.internalServerError().body(ex);
    }
}
