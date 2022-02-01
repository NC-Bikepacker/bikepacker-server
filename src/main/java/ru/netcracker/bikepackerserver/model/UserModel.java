package ru.netcracker.bikepackerserver.model;

import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserModel {
    private Optional<String> firstname;
    private Optional<String> lastname;
    private Optional<String> email;
    private Optional<String> nickname;
    private Optional<String> userPicLink;
    private Optional<Long> id;

    public UserModel() {
    }

    public static UserModel toModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setFirstname(entity.getFirstname());
        model.setLastname(entity.getLastname());
        model.setNickname(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setId(entity.getId());
        model.setUserPicLink(entity.getAvatarImageUrl());
        return model;
    }

    public Optional<String> getFirstname() {
        return firstname;
    }

    public Optional<String> getLastname() {
        return lastname;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getNickname() {
        return nickname;
    }

    public Optional<String> getUserPicLink() {
        return userPicLink;
    }

    public Optional<Long> getId() {
        return id;
    }

    public void setFirstname(String firstname) {
        this.firstname = Optional.ofNullable(firstname);
    }

    public void setLastname(String lastname) {
        this.lastname = Optional.ofNullable(lastname);
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
    }

    public void setNickname(String nickname) {
        this.nickname = Optional.ofNullable(nickname);
    }

    public void setUserPicLink(String userPicLink) {
        this.userPicLink = Optional.ofNullable(userPicLink);
    }

    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getFirstname(), userModel.getFirstname()) && Objects.equals(getLastname(), userModel.getLastname()) && getEmail().equals(userModel.getEmail()) && getNickname().equals(userModel.getNickname()) && Objects.equals(getUserPicLink(), userModel.getUserPicLink()) && getId().equals(userModel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getEmail(), getNickname(), getUserPicLink(), getId());
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname=" + firstname +
                ", lastname=" + lastname +
                ", email=" + email +
                ", nickname=" + nickname +
                ", userPicLink=" + userPicLink +
                ", id=" + id +
                '}';
    }
}