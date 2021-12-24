package ru.netcracker.bikepackerserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exceptions.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exceptions.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exceptions.UserNotFoundException;
import ru.netcracker.bikepackerserver.exceptions.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.User;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for operations on users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Post request for creating new user.
     * @param userEntity
     * @return http status.
     */
    @PostMapping
    public ResponseEntity create(@RequestBody UserEntity userEntity) {
        try {
            userService.create(userEntity);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("An error occurred while executing the request. \n" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get request for getting all users list.
     * @return users list and http status if list is not empty.
     * @return http status not found if it is.
     */
    @GetMapping
    public ResponseEntity read() {
        try {
            Iterable<UserEntity> users = userService.readAll();
            return new ResponseEntity(users, HttpStatus.OK);
        } catch (NoAnyUsersException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("An error occurred while executing the request. \n" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get request for getting user by his id.
     * @param id
     * @return user and http status "200 OK" if user in not null.
     * @return http status "404 Not Found" if user equals null.
     */
    @GetMapping("{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") Long id) {
        try {
            UserEntity user = userService.read(id);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("An error occurred while executing the request. \n" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Put request for updating user's data.
     * @param id
     * @param newUser
     * @return http status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User newUser) {
        try {
//            TODO: Fix this method
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Delete request for user deleting.
     * @param id
     * @return http status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteById(@PathVariable("id") Long id) {
        try {
            boolean isDeleted = userService.deleteById(id);
            if (isDeleted) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Delete request for user deleting.
     * @return http status.
     */
    @DeleteMapping("/all")
    public ResponseEntity<User> deleteAll() {
        try {
            boolean wereDeleted = userService.deleteAll();
            if (wereDeleted) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }


    @ControllerAdvice
    public class Handler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
            if (ex instanceof NullPointerException) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}