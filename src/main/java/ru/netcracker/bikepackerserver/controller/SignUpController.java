package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;


@RestController
@Api(tags = {"Sign up controller: registering a new Bikepacker user"})
@Validated
public class SignUpController {

    private final UserServiceImpl userService;

    public SignUpController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Register a new user", notes = "This request registers a new Bikepacker user")
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
}
