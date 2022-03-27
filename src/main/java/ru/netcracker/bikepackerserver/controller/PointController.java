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
        try {
            pointService.save(pointModel);
        } catch (Exception e) {
            LoggerFactory.getLogger(ResponseEntity.class).error("Bad request: ", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
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
        try {
            pointService.saveAll(pointModels);
        } catch (Exception e) {
            LoggerFactory.getLogger(ResponseEntity.class).error("Bad request: ", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
