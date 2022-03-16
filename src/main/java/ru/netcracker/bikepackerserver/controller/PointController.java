package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.service.PointServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/points")
@Api(tags = {"Point controller: saving and getting points"})
@Validated
public class PointController {

    @Autowired
    private final PointServiceImpl pointService;

    public PointController(PointServiceImpl pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/{trackId}")
    @ApiOperation(value = "Save a new point", notes = "This request saves a new point into database")
    public ResponseEntity savePoint(
        @ApiParam(
            name = "point",
            type = "PointModel",
            value = "Point model",
            required = true
        )
        @PathVariable Long trackId,
        @RequestBody @Valid PointModel pointModel) {
        pointService.save(pointModel, trackId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get all points", notes = "This request returns all points from database")
    public ResponseEntity getAllPoints() {
        return new ResponseEntity(pointService.readAll(), HttpStatus.OK);
    }
}
