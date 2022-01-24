package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = {"Authorization controller: authorize in application"})
public class AuthorizationController {

    @GetMapping("/login")
    @ApiOperation(value = "Authorize in the app", notes = "This request will allow you to sign in to the app using your credentials.")
    public String login(Model model) {
        return "login";
    }
}