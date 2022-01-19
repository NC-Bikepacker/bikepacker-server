package ru.netcracker.bikepackerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepo userRepo;

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    /**
     * Post request for creating new user.
     *
     * @param userEntity
     * @return http status.
     */
    @PostMapping
    public ResponseEntity create(@RequestBody UserEntity userEntity) {
        BCryptPasswordEncoder encrypter = new BCryptPasswordEncoder(12);
        userEntity.setPassword(encrypter.encode(userEntity.getPassword()));
        userRepo.save(userEntity);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Get request for getting all users list.
     *
     * @return users list and http status if list is not empty.
     * @return http status not found if it is.
     */
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity(userService.readAll(), HttpStatus.OK);
    }

    /**
     * Get request for getting user by his id.
     *
     * @param id
     * @return user and http status "200 OK" if user in not null.
     * @return http status "404 Not Found" if user equals null.
     */
    @GetMapping("{id}")
    public ResponseEntity<UserModel> read(@PathVariable(name = "id") Long id) {
        return new ResponseEntity(userService.read(id), HttpStatus.OK);
    }

    /**
     * Put request for updating user's data.
     *
     * @param id
     * @param newUserModel
     * @return http status.
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
     * @return http status.
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
     * @return http status.
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
