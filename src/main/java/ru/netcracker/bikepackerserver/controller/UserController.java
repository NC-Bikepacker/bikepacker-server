package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Controller for operations on users.
 */
@RestController
@RequestMapping("/users")
@Api(tags = {"User controller: creating and getting user models"})
@Validated
public class UserController {

    private UserRepo userRepo;
    private final UserServiceImpl userService;

    public UserController(UserRepo userRepo, UserServiceImpl userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    /**
     * Post request for creating new user.
     *
     * @return response entity.
     */
    @PostMapping("/user")
    @ApiOperation(value = "Create a new user", notes = "This request creates a new Bikepacker user")
    public ResponseEntity create(
            @ApiParam(
                    name = "userEntity",
                    type = "UserEntity",
                    value = "User Entity",
                    required = true
            )
            @Valid @RequestBody UserEntity userEntity,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }
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
    @GetMapping(value = "/user/id")
    @ApiOperation(value = "Get a user by id", notes = "This request finds and returns a user model by his id")
    public ResponseEntity readById(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "1",
                    required = true
            )
            @RequestParam @Min(1) Long id
    ) {
        return new ResponseEntity(userService.readById(id), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his username.
     *
     * @param username
     * @return response entity.
     */
    @GetMapping(value = "/user/username")
    @ApiOperation(value = "Get a user by username", notes = "This request finds and returns a user model by his username")
    public ResponseEntity readByUsername(
            @ApiParam(
                    name = "username",
                    type = "String",
                    value = "Username",
                    example = "fedoro_79",
                    required = true
            )
            @RequestParam @Valid String username
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
    @PutMapping(value = "/user/id")
    @ApiOperation(value = "Update a user profile data", notes = "This request changes current user data")
    public ResponseEntity update(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "11",
                    required = true
            )
            @RequestParam @Min(1) Long id,
            @ApiParam(
                    name = "userModel",
                    type = "UserModel",
                    value = "New user model. New user fields to be updated"
            )
            @RequestBody @Valid UserModel userModel
    ) {
        if (userService.update(userModel, id)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("Updating user was failed.", HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Delete request for user deleting.
     *
     * @param id
     * @return response entity.
     */
    @DeleteMapping("/user/id")
    @ApiOperation(value = "Delete a user by his id", notes = "This request deletes a user with a specific id.")
    public ResponseEntity deleteById(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "User id",
                    example = "11",
                    required = true
            )
            @RequestParam @Min(1) Long id
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
    @DeleteMapping("/user/username")
    @ApiOperation(value = "Delete a user by his username", notes = "This request deletes a user with a specific username.")
    public ResponseEntity deleteByUsername(
            @ApiParam(
                    name = "username",
                    type = "String",
                    value = "username",
                    example = "username",
                    required = true
            )
            @RequestParam @Valid String username
    ) {
        userService.deleteByUsername(username);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete request for user deleting.
     *
     * @return response entity.
     */
    @DeleteMapping("/all")
    @ApiOperation(value = "Delete all users", notes = "This method deletes all Bikepacker users")
    public ResponseEntity deleteAll() {
        if (userService.deleteAll()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("Deleting users was failed.", HttpStatus.NO_CONTENT);
        }
    }
}