package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;
import ru.netcracker.bikepackerserver.service.TrackServiceImpl;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/getonetrack/{id}")
    @ApiOperation(value = "get one track", notes = "This request getting one track")
    public ResponseEntity getOneTrack(
        @ApiParam(
                name = "id",
                type = "Long",
                value = "track id",
                example = "13",
                required = true
        )
        @PathVariable @Valid Long id
    ){
        if(id != null){
            return new ResponseEntity(trackService.getOneTrack(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getlastfriendtracks/{id}")
    @ApiOperation(value = "get one track", notes = "This request getting one track")
    public ResponseEntity getLastFriendTracks(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "track id",
                    example = "13",
                    required = true
            )
            @PathVariable @Valid Long id
    ){
        if(id != null){
            return new ResponseEntity(trackService.getLastFriendTracks(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity(HttpStatus.NOT_FOUND);
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