package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    /**
     * Creates a new user.
     *
     * @param entity
     */
    @Override
    public UserModel create(UserEntity entity) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        String username = "";
        String email = "";

        if (entity.getUsername().isPresent()) {
            username = entity.getUsername().get();
        }

        if (entity.getEmail().isPresent()) {
            email = entity.getEmail().get();
        }

        if (userRepo.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException(entity.getUsername().get());
        }

        if (userRepo.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException(entity.getEmail().get());
        }

        return UserModel.toModel(userRepo.save(entity));
    }

    /**
     * Returns list of all users.
     *
     * @return list of all users.
     */
    @Override
    public List<UserModel> readAll() throws NoAnyUsersException {
        Optional<List<UserEntity>> users = Optional.ofNullable(userRepo.findAll());

        if (users.isEmpty()) {
            throw new NoAnyUsersException();
        }

        List<UserModel> userModelList = new ArrayList<>();
        users.get().forEach(element -> userModelList.add(UserModel.toModel(element)));

        return userModelList;
    }

    /**
     * Returns user by his uuid.
     *
     * @param id
     * @return user by his uuid.
     */
    @Override
    public UserModel readById(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserModel.toModel(userEntity);
    }

    /**
     * Returns user by his username.
     *
     * @param username
     * @return user by his username.
     */
    @Override
    public UserModel readByUsername(String username) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return UserModel.toModel(userEntity);
    }

    /**
     * Updates user's info finding him by uuid
     *
     * @param newUser
     * @param id
     * @return true if user was updated and
     * false if didn't.
     */
    @Override
    public boolean update(UserModel newUser, Long id) throws UserNotFoundException {
        UserEntity dbUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (newUser.getFirstname().isPresent()) dbUser.setFirstname(newUser.getFirstname().get());
        if (newUser.getLastname().isPresent()) dbUser.setLastname(newUser.getLastname().get());
        if (newUser.getUsername().isPresent()) dbUser.setUsername(newUser.getUsername().get());
        if (newUser.getEmail().isPresent()) dbUser.setEmail(newUser.getEmail().get());
        if (newUser.getUserPicLink().isPresent()) dbUser.setAvatarImageUrl(newUser.getUserPicLink().get());

        userRepo.save(dbUser);

        return true;
    }

    /**
     * Delete user;
     *
     * @param id
     * @return true if user was deleted
     */
//    FIXME: ClassCastException
    @Override
    public boolean deleteById(Long id) throws UserNotFoundException {
        if (Optional.ofNullable(userRepo.findById(id)).isPresent()) {
            userRepo.deleteById(id);
            return true;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Delete user;
     *
     * @param username
     * @return true if user was deleted
     */
//    FIXME: and check if ot works
    @Override
    public boolean deleteByUsername(String username) throws UserNotFoundException {
        if (userRepo.findByUsername(username).isPresent()) {
            userRepo.deleteByUsername(username);
            return true;
        } else {
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Delete all users;
     *
     * @return true if user was deleted and
     * false if didn't.
     */

    @Override
    public boolean deleteAll() throws UsersDeletingException {
        if (userRepo.count() != 0) {
            userRepo.deleteAll();
            return true;
        } else {
            throw new UsersDeletingException();
        }
    }
}
