package ru.netcracker.bikepackerserver.controller;

import org.hibernate.annotations.Type;
import org.postgresql.jdbc.PgSQLXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLXML;
import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackRepo trackRepo;

    @GetMapping
    public List<TrackEntity> getTracks() {
        return trackRepo.findAll();
    }

    @PostMapping
    public ResponseEntity createTrack(@RequestBody TrackEntity track) throws URISyntaxException {
        TrackEntity savedTrack = trackRepo.save(track);
        return ResponseEntity.created(new URI("/tracks/" + savedTrack.getTrack_id())).body(savedTrack);
    }
}