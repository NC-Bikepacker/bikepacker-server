package ru.netcracker.bikepackerserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.entity.VerificationTokenEntity;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.repository.VerificationTokenRepo;
import ru.netcracker.bikepackerserver.service.OnRegistrationCompleteEvent;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

@Controller
@Api(tags = {"Sign up controller: registering a new Bikepacker user"})
@Validated
public class SignUpController {

    private final UserServiceImpl userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    @Autowired
    private UserRepo userRepo;

    public SignUpController(UserServiceImpl userService, ApplicationEventPublisher eventPublisher, VerificationTokenRepo verificationTokenRepo) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.verificationTokenRepo = verificationTokenRepo;
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
            @RequestBody UserEntity userEntity, HttpServletRequest request, Errors errors) {
        Optional<String> email = Optional.ofNullable(userEntity.getEmail());
        try {
            if(email.isEmpty()){throw new IllegalArgumentException();}
            BCryptPasswordEncoder encrypter = new BCryptPasswordEncoder(12);
            userEntity.setPassword(encrypter.encode(userEntity.getPassword()));
            userService.create(userEntity);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity, request.getLocale(), request.getContextPath()));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            LoggerFactory.getLogger(SignUpController.class).error(exception.getMessage(), exception);
            return new ResponseEntity("Registration error", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Confirm your account to complete registration, an email was sent to you: " + email.orElse("email@email.ru"), HttpStatus.CREATED);
    }

    @GetMapping("/registrationConfirm/{token}")
    public String confirmRegistration (@PathVariable @Valid String token) {

        VerificationTokenEntity verificationToken = verificationTokenRepo.findByToken(token);

        if (verificationToken == null) {
            return "verificationCodeIsMissing";
        }


        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return "verificationCodeTimeOut";
        }

        UserEntity user = verificationToken.getUser();
        user.setAccountVerification(true);
        userService.create(user);
        verificationTokenRepo.delete(verificationToken);
        return "confirmEmail";
    }

    @GetMapping("/repeatConfirm/{user_id}")
    public ResponseEntity repeatConfirm (@PathVariable @Valid Long user_id) {
        UserEntity user = userRepo.findByid(user_id);
        Optional<String> email;
        if(user == null){new UserNotFoundException(user_id);}
        assert user != null;
        if(user.isAccountVerification()){return new ResponseEntity("Your account does not need to be verified", HttpStatus.BAD_REQUEST);}
        email = Optional.ofNullable(user.getEmail());
        try {
            VerificationTokenEntity verificationToken = verificationTokenRepo.findByUser(user);
            if (verificationToken != null){
                    verificationTokenRepo.delete(verificationToken);
            }
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            LoggerFactory.getLogger(SignUpController.class).error(e.getMessage(), e);
            return new ResponseEntity("Confirmation link request error" , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Confirm your account to complete registration, an email was sent to you: " + email.orElse("email@email.ru"), HttpStatus.CREATED);
    }

    @PutMapping("/updateuserdata/{userId}")
    @ApiOperation(value = "Update  user data", notes = "This request update data a Bikepacker user")
    public ResponseEntity updateUserData(
            @ApiParam(
                    name = "userEntity",
                    type = "UserEntity",
                    value = "User Entity",
                    required = true
            )
            @PathVariable Long userId,
            @RequestBody UserEntity userEntity) {
        UserModel newUserData = new UserModel();
        try {
            newUserData = userService.updateUserData(userEntity, userId);
        }
        catch (Exception e){
            LoggerFactory.getLogger(SignUpController.class).error("Error update user data", e.getMessage(), e);
        }
        return new ResponseEntity(newUserData, HttpStatus.OK);
    }
}
