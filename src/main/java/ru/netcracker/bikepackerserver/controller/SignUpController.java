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
            return new ResponseEntity("Ошибка регистрации", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Для завершения регистрации подтвердите свою учетную запись, письмо отправлено на почту: " + email.orElse("email@email.ru"), HttpStatus.CREATED);
    }

    @GetMapping("/registrationConfirm/{token}")
    public String confirmRegistration (@PathVariable @Valid String token) {

        VerificationTokenEntity verificationToken = verificationTokenRepo.findByToken(token);

        if (verificationToken == null) {
            return "код c таким значением отсутствует";
        }


        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "время действия кода вышло, запросите код повторно ";
        }

        UserEntity user = verificationToken.getUser();
        user.setAccountVerification(true);
        userService.create(user);
        verificationTokenRepo.delete(verificationToken);
        return "confirmEmail";
    }
}
