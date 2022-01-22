package ru.netcracker.bikepackerserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

/**
 * Controller for operations on users.
 */
@RestController
@RequestMapping("/users")
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
    @PostMapping
    public ResponseEntity create(@RequestBody UserEntity userEntity) {
        BCryptPasswordEncoder encrypter = new BCryptPasswordEncoder(12);
        userEntity.setPassword(encrypter.encode(userEntity.getPassword().get()));
        userRepo.save(userEntity);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Get request for getting all users list.
     *
     * @return response entity.
     */
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity(userService.readAll(), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his id.
     *
     * @param id
     * @return response entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity readById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity(userService.readById(id), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his username.
     *
     * @param username
     * @return response entity.
     */
    @GetMapping("/{username}")
    public ResponseEntity readByUsername(@PathVariable(name = "username") String username) {
        return new ResponseEntity(userService.readByUsername(username), HttpStatus.OK);
    }

    /**
     * Put request for updating user's data.
     *
     * @param id
     * @param newUserModel
     * @return response entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody UserModel newUserModel) {
        if (userService.update(newUserModel, id)) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id) {
        if (userService.deleteById(id)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("Deleting user was failed.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete request for user deleting.
     *
     * @return response entity.
     */
    @DeleteMapping("/all")
    public ResponseEntity deleteAll() {
        if (userService.deleteAll()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("Deleting users was failed.", HttpStatus.NO_CONTENT);
        }
    }
}
