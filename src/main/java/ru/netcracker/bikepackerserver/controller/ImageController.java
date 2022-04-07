package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/image")
@Validated
@Api(tags = {"Image controller: creating and getting image"})
public class ImageController {
    private UserRepo userRepo;
    private TrackRepo trackRepo;
    private ImageRepo imageRepo;

    public ImageController(UserRepo userRepo, TrackRepo trackRepo, ImageRepo imageRepo) {
        this.userRepo = userRepo;
        this.trackRepo = trackRepo;
        this.imageRepo = imageRepo;
    }

    @PostMapping
    @ApiOperation(value = "Create a new track", notes = "This request creates a new track")
    public ResponseEntity createImage(
            @RequestBody
            @ApiParam(
                    name = "Image Entity",
                    type = "ImageEntity",
                    value = "Track Entity",
                    required = true
            )
                    ImageEntity image
    ) throws URISyntaxException {
        ImageEntity savedImage = imageRepo.save(image);
        return ResponseEntity.created(new URI("/tracks/" + savedImage.getImageId())).body(savedImage);
    }

    @GetMapping(value = "/user/{user_id}")
    @ApiOperation(value = "Get a image by username", notes = "This request finds and returns a user by his username")
    public ResponseEntity getImageForUser(
            @ApiParam(
                    name = "user_id",
                    type = "Long",
                    value = "user id",
                    example = "5",
                    required = true
            )
            @PathVariable @Valid Long user_id
    ) {
        UserEntity user = userRepo.findByid(user_id);
        return new ResponseEntity(imageRepo.findByUser(user), HttpStatus.OK);
    }

    @GetMapping(value = "/track/{track_id}")
    @ApiOperation(value = "Get a image by username", notes = "This request finds and returns a user by his username")
    public ResponseEntity getImageForTrack(
            @ApiParam(
                    name = "track_id",
                    type = "Long",
                    value = "track id",
                    example = "5",
                    required = true
            )
            @PathVariable @Valid Long track_id
    ) {
        TrackEntity track = trackRepo.getById(track_id);
        return new ResponseEntity(imageRepo.findByTrack(track), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get all users", notes = "This request return all Bikepacker users")
    public ResponseEntity read() {
        return new ResponseEntity(imageRepo.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete a user by his id", notes = "This request deletes a user with a specific id.")
    public ResponseEntity deleteById(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "favorite track id",
                    example = "11",
                    required = true
            )
            @PathVariable Long id
    ) {
        imageRepo.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
