package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.validation.Valid;

/**
 * Controller for operations on users.
 */
@RestController
@RequestMapping("/users")
@Api(tags = {"User controller: creating and getting user models"})
@Validated
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Post request for creating new user.
     *
     * @return response entity.
     */
    @PostMapping("/user/create")
    @ApiOperation(value = "Create a new user", notes = "This request creates a new Bikepacker user")
    public ResponseEntity create(
            @ApiParam(
                    name = "userEntity",
                    type = "UserEntity",
                    value = "User Entity",
                    required = true
            )
            @RequestBody UserEntity userEntity) {
        BCryptPasswordEncoder encrypter = new BCryptPasswordEncoder(12);
        userEntity.setPassword(encrypter.encode(userEntity.getPassword()));
        userService.create(userEntity);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Get request for getting all users list.
     *
     * @return response entity.
     */
    @GetMapping
    @ApiOperation(value = "Get all users", notes = "This request return all Bikepacker users")
    public ResponseEntity read() {
        return new ResponseEntity(userService.readAll(), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his id.
     *
     * @param id
     * @return response entity.
     */
    @GetMapping(value = "/user/getbyid/{id}")
    @ApiOperation(value = "Get a user by id", notes = "This request finds and returns a user model by his id")
    public ResponseEntity readById(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return new ResponseEntity(userService.readById(id), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his username.
     *
     * @param username
     * @return response entity.
     */
    @GetMapping(value = "/user/getbyusername/{username}")
    @ApiOperation(value = "Get a user by username", notes = "This request finds and returns a user model by his username")
    public ResponseEntity readByUsername(
            @ApiParam(
                    name = "username",
                    type = "String",
                    value = "Username",
                    example = "fedoro_79",
                    required = true
            )
            @PathVariable @Valid String username
    ) {
        return new ResponseEntity(userService.readByUsername(username), HttpStatus.OK);
    }

    /**
     * Put request for updating user's data.
     *
     * @param id
     * @param userModel
     * @return response entity.
     */
    @PutMapping(value = "/user/updatebyid/{id}")
    @ApiOperation(value = "Update a user profile data", notes = "This request changes current user data")
    public ResponseEntity update(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "11",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody @Valid UserModel userModel
    ) {
        userService.update(userModel, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete request for user deleting.
     *
     * @param id
     * @return response entity.
     */
    @DeleteMapping("/user/deletebyid/{id}")
    @ApiOperation(value = "Delete a user by his id", notes = "This request deletes a user with a specific id.")
    public ResponseEntity deleteById(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "11",
                    required = true
            )
            @PathVariable Long id
    ) {
        userService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete request for user deleting.
     *
     * @param username
     * @return response entity.
     */
    @DeleteMapping("/user/deletebyusername/{username}")
    @ApiOperation(value = "Delete a user by his username", notes = "This request deletes a user with a specific username.")
    public ResponseEntity deleteByUsername(
            @ApiParam(
                    name = "username",
                    type = "String",
                    value = "username",
                    example = "username",
                    required = true
            )
            @PathVariable String username
    ) {
        userService.deleteByUsername(username);
        return new ResponseEntity(HttpStatus.OK);
    }
}