package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/tracks")
@Validated
@Api(tags = {"Track controller: creating and getting tracks"})
public class TrackController {

    private final TrackRepo trackRepo;

    public TrackController(TrackRepo trackRepo) {
        this.trackRepo = trackRepo;
    }

    @GetMapping
    @ApiOperation(value = "Get all tracks in the app", notes = "This request returns a list of all of the tracks in DB")
    public List<TrackEntity> getTracks() {
        return trackRepo.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Create a new track", notes = "This request creates a new track")
    public ResponseEntity createTrack(
            @RequestBody
            @ApiParam(
                    name = "Track Entity",
                    type = "TrackEntity",
                    value = "Track Entity",
                    required = true
            )
                    TrackEntity track
    ) throws URISyntaxException {
        TrackEntity savedTrack = trackRepo.save(track);
        return ResponseEntity.created(new URI("/tracks/" + savedTrack.getTrackId())).body(savedTrack);
    }
}