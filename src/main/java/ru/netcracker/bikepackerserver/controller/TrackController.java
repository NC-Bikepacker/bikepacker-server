package ru.netcracker.bikepackerserver.controller;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.LoggerFactory;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tracks")
@Validated
@Api(tags = {"Track controller: creating and getting tracks"})
public class TrackController {

    private TrackServiceImpl trackService;

    @Autowired
    public TrackController(TrackServiceImpl trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    @ApiOperation(value = "Get all tracks in the app", notes = "This request returns a list of all of the tracks in DB")
    public List<TrackModel> getTracks() {
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTrackByUser(@PathVariable(name = "id") Long id) {
        if (id != null) {
            return new ResponseEntity(trackService.getTracksForUser(id), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bydistance/{userid}/{lat}/{lon}")
    public ResponseEntity getTracksSortedByDistance(@PathVariable(name = "userid") Long userId,
                                                    @PathVariable(name = "lat") double lat,
                                                    @PathVariable(name = "lon") double lon) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            WayPoint location = WayPoint.of(lat, lon);
            Map<Double, TrackModel> tracksMap = new HashMap<>();
            LoggerFactory.getLogger(TrackController.class).debug(tracks.toString());
            for (TrackModel trackModel : tracks) {
                GPX gpx = null;
                try {
                    String gpxStr = trackModel.getGpx();
                    if (gpxStr != null) {
                        gpx = GPX.read(new ByteArrayInputStream(gpxStr.getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException e) {
                    LoggerFactory.getLogger(TrackController.class).error(e.getMessage(),e);
                }
                if (gpx != null) {
                    for (Track track : gpx.getTracks()) {
                        if (track != null) {
                            for (TrackSegment segment : track.getSegments()) {
                                WayPoint start;
                                Optional<WayPoint> optional = segment.getPoints().stream().findFirst();
                                if (optional.isPresent())
                                    start = optional.get();
                                else continue;
                                double length = Geoid.WGS84.distance(start, location).doubleValue();
                                tracksMap.put(length, trackModel);
                            }
                        }
                    }
                }
            }
            List<TrackModel> result = new ArrayList<>();
            List<Double> keys = tracksMap.keySet().stream().sorted(Double::compareTo).collect(Collectors.toList());
            for (Double key : keys) {
                result.add(tracksMap.get(key));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bycomplexity/{userid}")
    public ResponseEntity getTracksSortedByComplexity(@PathVariable(name = "userid") Long userId) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            List<TrackModel> result = tracks.stream()
                    .sorted(Comparator.comparingDouble(TrackModel::getTrackComplexity))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bytime/{userid}")
    public ResponseEntity getTracksSortedByTime(@PathVariable(name = "userid") Long userId) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            List<TrackModel> result = tracks.stream()
                    .sorted(Comparator.comparingLong(TrackModel::getTravelTime))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    ) {
        if (id != null) {
            return new ResponseEntity(trackService.getOneTrack(id), HttpStatus.OK);
        } else {
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
    ) {
        if (id != null) {
            return new ResponseEntity(trackService.getLastFriendTracks(id), HttpStatus.OK);
        } else {
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
            ) TrackModel track
    ) {
        try {
            return new ResponseEntity(trackService.save(track).getTrackId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTrack(@PathVariable(name = "id") Long id) {
        try {
            trackService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
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