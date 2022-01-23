package ru.netcracker.bikepackerserver.controller;

import org.hibernate.annotations.Type;
import org.postgresql.jdbc.PgSQLXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLXML;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackRepo trackRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<TrackEntity> getTracks() {
        return trackRepo.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity read(@PathVariable(name = "id") Long id) {
        Optional<UserEntity> user = userRepo.findById(id);
        if(user.isPresent()){
           return new ResponseEntity(trackRepo.findByUser(user), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity createTrack(@RequestBody TrackEntity track) throws URISyntaxException {
        TrackEntity savedTrack = trackRepo.save(track);
        return ResponseEntity.created(new URI("/tracks/" + savedTrack.getTrack_id())).body(savedTrack);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTrack(@PathVariable(name = "id") Long id){
        try {
            trackRepo.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}