package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exception.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.exception.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.UserModel;

import java.util.List;

public interface UserService {

    void create(UserEntity user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    List<UserModel> readAll() throws NoAnyUsersException;

    UserModel readById(Long id) throws UserNotFoundException;

    UserModel readByUsername(String username);

    void update(UserModel userModel, Long id);

    void deleteById(Long id);

    void deleteByUsername(String username);

    boolean deleteAll();
}
