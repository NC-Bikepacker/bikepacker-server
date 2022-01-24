package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Administration profile controller"})
public class AdministrationProfileController {

    @GetMapping("/admin")
    @ApiOperation(value = "Admin controller", notes = "")
    public String admin(Model model) {
        return "<h1>Welcome, admin!</h1>";
    }
}
