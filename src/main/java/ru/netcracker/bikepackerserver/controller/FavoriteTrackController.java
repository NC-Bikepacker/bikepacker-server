package ru.netcracker.bikepackerserver.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.FavoriteTrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.FavoriteTrackRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.FavoriteTrackService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/favoritetracks")
@Validated
@Api(tags = {"Favorite track controller: creating and getting favorite tracks"})
public class FavoriteTrackController {
    private FavoriteTrackRepo favoriteTrackRepo;
    private TrackRepo trackRepo;
    private UserRepo userRepo;
    private FavoriteTrackService favoriteTrackService;

    @Autowired
    public FavoriteTrackController(FavoriteTrackRepo favoriteTrackRepo, TrackRepo trackRepo, UserRepo userRepo, FavoriteTrackService favoriteTrackService) {
        this.favoriteTrackRepo = favoriteTrackRepo;
        this.trackRepo = trackRepo;
        this.userRepo = userRepo;
        this.favoriteTrackService = favoriteTrackService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new track", notes = "This request creates a new track")
    public ResponseEntity createFavoriteTrack(
            @RequestBody
            @ApiParam(
                    name = "Favorite Track Entity",
                    type = "FavoriteTrackEntity",
                    value = "Favorite Track Entity",
                    required = true
            )
                    FavoriteTrackEntity favoriteTrack
    ) throws URISyntaxException {
        FavoriteTrackEntity savedFavoriteTrack = favoriteTrackRepo.save(favoriteTrack);
        return ResponseEntity.created(new URI("/tracks/" + savedFavoriteTrack.getFavoriteTrackId())).body(favoriteTrack);
    }

    @GetMapping("/{id}")
    public ResponseEntity getByUserId(@PathVariable(name = "id") Long userId) {
        if (userId != null) {
            return new ResponseEntity(favoriteTrackService.getTracksByUserId(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @ApiOperation(value = "Get all tracks in the app", notes = "This request returns a list of all of the tracks in DB")
    public ResponseEntity getTracks() {
        return new ResponseEntity(favoriteTrackService.getTracks(), HttpStatus.OK);
    }
}
