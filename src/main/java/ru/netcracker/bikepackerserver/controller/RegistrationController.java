package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = {"Registration controller: register new users"})
public class RegistrationController {

    @GetMapping("/registration")
    @ApiOperation(value = "Register a new user", notes = "This request registers a new Bikepacker user")
    public String registration(Model model) {
        return "registration";
    }
}
