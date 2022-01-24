package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exception.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.exception.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.UserModel;

import java.util.List;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user
     */
    public UserModel create(UserEntity user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    /**
     * Returns list of all users.
     *
     * @return
     */
    public List<UserModel> readAll() throws NoAnyUsersException;

    /**
     * Returns user by his uuid.
     *
     * @param id
     * @return user by his uuid.
     */
    UserModel readById(Long id) throws UserNotFoundException;

    /**
     * Returns user by his username.
     *
     * @param username
     * @return user by his username.
     */
    public UserModel readByUsername(String username);

    /**
     * Updates user's info finding him by uuid
     *
     * @param userModel
     * @param id
     * @return true if user was updated and
     * false if didn't.
     */
    boolean update(UserModel userModel, Long id);

    /**
     * Delete user;
     *
     * @param id
     * @return true if user was deleted and
     * false if didn't.
     */
    boolean deleteById(Long id);


    /**
     * Delete user;
     *
     * @param username
     * @return true if user was deleted and
     * false if didn't.
     */
    boolean deleteByUsername(String username);

    /**
     * Delete all users;
     *
     * @return true if user was deleted and
     * false if didn't.
     */
    boolean deleteAll();
}
