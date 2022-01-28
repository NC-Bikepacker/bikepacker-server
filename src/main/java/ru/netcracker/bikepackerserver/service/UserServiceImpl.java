package ru.netcracker.bikepackerserver.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Creates a new user.
     *
     * @param entity
     */
    @Override
    public UserModel create(UserEntity entity) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        String username = "";
        String email = "";

        if (entity.getUsername() != null) {
            username = entity.getUsername();
        }

        if (entity.getEmail() != null) {
            email = entity.getEmail();
        }

        if (userRepo.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException(entity.getUsername());
        }

        if (userRepo.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException(entity.getEmail());
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
        List<UserEntity> users = userRepo.findAll();

        if (users.isEmpty()) {
            throw new NoAnyUsersException();
        }

        List<UserModel> userModelList = new ArrayList<>();
        users.forEach(element -> userModelList.add(UserModel.toModel(element)));

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

        if (newUser.getFirstname() != null && !newUser.getFirstname().equals("")) {
            dbUser.setFirstname(newUser.getFirstname());
        }

        if (newUser.getLastname() != null && !newUser.getLastname().equals("")) {
            dbUser.setLastname(newUser.getLastname());
        }

        if (newUser.getUsername() != null && !newUser.getUsername().equals("")) {
            dbUser.setUsername(newUser.getUsername());
        }

        if (newUser.getEmail() != null && !newUser.getEmail().equals("")) {
            dbUser.setEmail(newUser.getEmail());
        }

        if (newUser.getUserPicLink() != null && !newUser.getUserPicLink().equals("")) {
            dbUser.setAvatarImageUrl(newUser.getUserPicLink());
        }

        userRepo.save(dbUser);
        return true;
    }

    /**
     * Delete user by id;
     *
     * @param id
     * @return true if user was deleted
     */
    @Override
    public void deleteById(Long id) throws UserNotFoundException {
            userRepo.deleteById(id);
    }

    /**
     * Delete user by username;
     *
     * @param username
     * @return true if user was deleted
     */
    @Transactional
    @Modifying
    @Query(value =
            "delete " +
            "from UserEntity ue " +
            "where ue.username = :username"
    )
    public void deleteByUsername(@Param("username") String username) {}

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
