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

@RestController
@RequestMapping("/users")
@Api(tags = {"User controller: creating and getting user models"})
@Validated
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation(value = "Get all users", notes = "This request return all Bikepacker users")
    public ResponseEntity read() {
        return new ResponseEntity(userService.readAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/getbyid/{id}")
    @ApiOperation(value = "Get a user by id", notes = "This request finds and returns a user by his id")
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

    @GetMapping(value = "/user/getbyusername/{username}")
    @ApiOperation(value = "Get a user by username", notes = "This request finds and returns a user by his username")
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

    @GetMapping(value = "/user/getbyemail/{email}")
    @ApiOperation(value = "Get a user by email", notes = "This request finds and returns a user by his email")
    public ResponseEntity readByEmail(
            @ApiParam(
                    name = "email",
                    type = "String",
                    value = "Email",
                    example = "ad-ryaz@yandex.ru",
                    required = true
            )
            @PathVariable @Valid String email
    ) {
        return new ResponseEntity(userService.readByEmail(email), HttpStatus.OK);
    }

    @PutMapping(value = "/user/updatebyid/{id}")
    @ApiOperation(value = "Update a user profile data", notes = "This request changes current user data")
    public ResponseEntity updateById(
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

    @PutMapping(value = "/user/updatebyemail/{email}")
    @ApiOperation(value = "Update a user profile data", notes = "This request changes current user data")
    public ResponseEntity updateById(
            @ApiParam(
                    name = "email",
                    type = "Long",
                    value = "User email",
                    example = "ad-ryaz@yandex.ru",
                    required = true
            )
            @PathVariable String email,
            @RequestBody @Valid UserModel userModel
    ) {
        userService.update(userModel, email);
        return new ResponseEntity(HttpStatus.OK);
    }

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