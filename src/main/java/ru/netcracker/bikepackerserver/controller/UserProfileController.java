package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"User profile controller: getting user profile data"})
public class UserProfileController {

    @GetMapping("/user")
    @ApiOperation(value = "Get an user profile information", notes = "")
    public String user(Model model) {
        return "<h1>Welcome, user!</h1>";
    }
}