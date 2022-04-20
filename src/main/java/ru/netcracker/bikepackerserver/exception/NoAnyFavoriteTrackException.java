package ru.netcracker.bikepackerserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoAnyFavoriteTrackException extends BaseException{

     public NoAnyFavoriteTrackException(){
       httpStatus = HttpStatus.NOT_FOUND;
       description = "FavoriteTrack not found.";
   }
}
