package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exceptions.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exceptions.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exceptions.UserNotFoundException;
import ru.netcracker.bikepackerserver.exceptions.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Creates a new user.
     * @param user
     */
    public UserEntity create(UserEntity user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    /**
     * Returns list of all users.
     * @return
     */
    public Iterable<UserEntity> readAll() throws NoAnyUsersException;

    /**
     * Returns user by his uuid.
     * @param id
     * @return user by his uuid.
     */
    UserEntity read(Long id) throws UserNotFoundException;

    /**
     * Updates user's info finding him by uuid
     * @param user
     * @param id
     * @return true if user was updated and
     *         false if didn't.
     */
    boolean update(User user, Long id);

    /**
     * Delete user;
     * @param id
     * @return true if user was deleted and
     *         false if didn't.
     */
    boolean deleteById(Long id) throws Exception;

    /**
     * Delete all users;
     * @return true if user was deleted and
     *         false if didn't.
     */
    boolean deleteAll() throws Exception;
}
