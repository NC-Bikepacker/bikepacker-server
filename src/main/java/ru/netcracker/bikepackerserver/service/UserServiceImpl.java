package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.entity.VerificationTokenEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.repository.VerificationTokenRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void create(UserEntity entity) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        userRepo.save(entity);
    }

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

    @Override
    public UserModel readById(Long id) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findById(id);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return UserModel.toModel(userEntity.get());
    }

    @Override
    public UserModel readByUsername(String username) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return UserModel.toModel(userEntity);
    }

    public UserModel readByEmail(String email) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findByEmail(email);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException(email);
        }

        return UserModel.toModel(userEntity.get());
    }

    public List<UserEntity> searchByFirstLastName(String name) {
        List<UserEntity> users = userRepo.findAll();
        List<UserEntity> res = new ArrayList<>();
        for (UserEntity user : users
        ) {
            if (contains(name, user.getFirstname()) || contains(name, user.getLastname()))
                res.add(user);
        }
        return res;
    }

    @Override
    public void update(UserModel model, Long id) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(Long.toString(id)));
        updateEntity(model, userEntity);
        userRepo.save(userEntity);
    }

    public void update(UserModel model, String email) {
        UserEntity userEntity = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        updateEntity(model, userEntity);
        userRepo.save(userEntity);
    }

    @Override
    public void deleteById(Long id) throws UserNotFoundException {
        userRepo.deleteById(id);
    }

    public void deleteByUsername(String username) {
        userRepo.deleteByUsername(username);
    }

    public void deleteByEmail(String email) {
        userRepo.deleteByEmail(email);
    }

    @Override
    public boolean deleteAll() throws UsersDeletingException {
        if (userRepo.count() != 0) {
            userRepo.deleteAll();
            return true;
        } else {
            throw new UsersDeletingException();
        }
    }

    @Override
    public void createVerificationToken(UserEntity user, String token) {
        VerificationTokenEntity myToken = new VerificationTokenEntity(token, user);
        verificationTokenRepo.save(myToken);
    }


    @Override
    public UserEntity getUserByVerificationToken(String VerificationToken) {
        VerificationTokenEntity verificationToken = verificationTokenRepo.findByToken(VerificationToken);
        return userRepo.findById(verificationToken.getUser().getId()).get();

    }

    private void updateEntity(UserModel model, UserEntity userEntity) {
        if (!model.getFirstname().equals("")) userEntity.setFirstname(model.getFirstname());
        if (!model.getLastname().equals("")) userEntity.setLastname(model.getLastname());
        if (!model.getUsername().equals("")) userEntity.setUsername(model.getUsername());
        if (!model.getEmail().equals("")) userEntity.setEmail(model.getEmail());
        if (!model.getUserPicLink().equals("")) userEntity.setAvatarImageUrl(model.getUserPicLink());
    }

    public boolean contains(String part, String user) {
        String REGEX_FIND_WORD = "(?i).*?" + part + ".*?";
        String regex = String.format(REGEX_FIND_WORD, Pattern.quote(part));
        if (user.matches(regex)) {
            return true;
        }
        return false;
    }
}