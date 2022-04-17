package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;
import ru.netcracker.bikepackerserver.service.TrackServiceImpl;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tracks")
@Validated
@Api(tags = {"Track controller: creating and getting tracks"})
public class TrackController {

    private TrackRepo trackRepo;
    private UserRepo userRepo;
    private TrackImageService trackImageService;
    private TrackServiceImpl trackService;

    @Autowired
    public TrackController(TrackRepo trackRepo, UserRepo userRepo, TrackImageService trackImageService, TrackServiceImpl trackService) {
        this.trackRepo = trackRepo;
        this.userRepo = userRepo;
        this.trackImageService = trackImageService;
        this.trackService = trackService;
    }

    @GetMapping
    @ApiOperation(value = "Get all tracks in the app", notes = "This request returns a list of all of the tracks in DB")
    public List<TrackModel> getTracks() {
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTrackByUser(@PathVariable(name = "id") Long id) {
        if(id != null){
            return new ResponseEntity(trackService.getTracksForUser(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create a new track", notes = "This request creates a new track")
    public ResponseEntity createTrack(
            @RequestBody
            @ApiParam(
                    name = "Track Model",
                    type = "TrackModel",
                    value = "TrackModel",
                    required = true
            )
                    TrackModel track
    ) throws Exception {
        return new ResponseEntity(trackService.save(track).getTrackId(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTrack(@PathVariable(name = "id") Long id){
        try {
            trackService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update a track data", notes = "This request changes current track")
    public ResponseEntity updateTrack(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "134",
                    example = "134",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody TrackModel trackModel

    ) {
        trackService.update(trackModel);
        return new ResponseEntity(HttpStatus.OK);
    }
}