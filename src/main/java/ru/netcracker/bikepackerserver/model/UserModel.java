package ru.netcracker.bikepackerserver.model;

import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.Objects;

public class UserModel {
    private String firstname;
    private String lastname;
    private String email;
    private String nickname;
    private String userPicLink;
    private Long id;

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPicLink() {
        return userPicLink;
    }

    public void setUserPicLink(String userPicLink) {
        this.userPicLink = userPicLink;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getFirstname(), userModel.getFirstname()) && Objects.equals(getLastname(), userModel.getLastname()) && Objects.equals(getEmail(), userModel.getEmail()) && Objects.equals(getNickname(), userModel.getNickname()) && Objects.equals(getUserPicLink(), userModel.getUserPicLink()) && Objects.equals(getId(), userModel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getEmail(), getNickname(), getUserPicLink(), getId());
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userPicLink='" + userPicLink + '\'' +
                ", id=" + id +
                '}';
    }
}