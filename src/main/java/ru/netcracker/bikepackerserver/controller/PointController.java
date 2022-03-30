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
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.PointRepo;
import ru.netcracker.bikepackerserver.service.PointServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/points")
@Api(tags = {"Point controller: saving and getting points"})
@Validated
public class PointController {

    @Autowired
    private final PointServiceImpl pointService;

    @Autowired
    private final PointRepo pointRepo;

    public PointController(PointServiceImpl pointService, PointRepo pointRepo) {
        this.pointService = pointService;
        this.pointRepo = pointRepo;
    }

    @PostMapping("/point")
    @ApiOperation(value = "Save a new point", notes = "This request saves a new point into database")
    public ResponseEntity savePoint(
        @ApiParam(
                name = "point",
                type = "PointModel",
                value = "Point model",
                required = true
        )
        @RequestBody @Valid PointModel pointModel) {
        pointService.save(pointModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "Save points", notes = "This request saves several points into database")
    public ResponseEntity savePoints(
        @ApiParam(
                name = "point",
                type = "PointModel",
                value = "Point models",
                required = true
        )
        @RequestBody @Valid List<PointModel> pointModels) {
        pointService.saveAll(pointModels);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/trackid/{trackId}")
    @ApiOperation(value = "Get points by track id", notes = "This query returns all points related to a particular track by its id.")
    public ResponseEntity getPointsByTrackId(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "Track id",
                    example = "1",
                    required = true
            )
            @PathVariable Long trackId) {
        List<PointModel> points = new ArrayList<>();
        points = pointService.getPointModelsByTrackId(trackId);
        return new ResponseEntity(points, HttpStatus.OK);
    }

    @GetMapping("/point/pointid/{pointId}")
    @ApiOperation(value = "Get point by point id", notes = "This query returns the point its id.")
    public ResponseEntity getPointByPointId(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "Point id",
                    example = "1",
                    required = true
            )
            @PathVariable Long pointId) {
        PointModel point = null;
        point = pointService.getPointModelById(pointId);
        return new ResponseEntity(point, HttpStatus.OK);
    }

    @GetMapping("/coordinates")
    @ApiOperation(value = "Get points with particular latitude and longitude", notes = "This query returns all points that lie within the area bounded by the given coordinates.")
    public ResponseEntity getPointsFromArea(
            @ApiParam(
                    name = "latitude-start",
                    type = "double",
                    value = "Starting latitude point",
                    example = "10.5",
                    required = true
            )
            @RequestParam(name = "latitude-start") double latitudeStart,
            @ApiParam(
                    name = "latitude-end",
                    type = "double",
                    value = "Ending latitude point",
                    example = "100.5",
                    required = true
            )
            @RequestParam(name = "latitude-end") double latitudeEnd,
            @ApiParam(
                    name = "longitude-start",
                    type = "double",
                    value = "Starting longitude point",
                    example = "40.75",
                    required = true
            )
            @RequestParam(name = "longitude-start") double longitudeStart,
            @ApiParam(
                    name = "longitude-end",
                    type = "double",
                    value = "Ending longitude point",
                    example = "50.75",
                    required = true
            )
            @RequestParam(name = "longitude-end") double longitudeEnd) {
        List<PointModel> points;
        points = pointService.getPointModelsByCoordinates(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd);
        return new ResponseEntity(points, HttpStatus.OK);
    }

    @GetMapping("/description")
    @ApiOperation(value = "Find points by description", notes = "This query returns all points that contain the string you are looking for.")
    public ResponseEntity getPointsByDescription(
            @ApiParam(
                    name = "description",
                    type = "String",
                    value = "Text",
                    example = "Гото Предестинация",
                    required = true
            )
            @RequestParam(name = "text") String description) {
        List<PointModel> points = null;
        points = pointService.getPointModelsByDescription(description);
        return new ResponseEntity(points, HttpStatus.OK);
    }
}
