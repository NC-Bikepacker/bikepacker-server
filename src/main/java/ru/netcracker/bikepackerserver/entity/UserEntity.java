package ru.netcracker.bikepackerserver.entity;

import javax.persistence.*;

//@Entity
//@Table(name="user_short")
//public class UserEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long id;
//
//    @Column(name = "first_name")
//    private String firstname;
//
//    @Column(name = "last_name")
//    private String lastname;
//
//    public UserEntity() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//}

@Entity
@Table(name="`user`")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "nickname")
    private String username;

    @Column(name = "password")
    private String password;

//    @Column(name = "avatar_image_URL")
//    private String avatarImageUrl;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "email")
    private String email;

    public UserEntity() {
    }

    public Long getId() {
        return id;
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

    public String getUsername() {
        return username;
    }

    public void setUserName(String nickname) {
        this.username = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getAvatarImageUrl() {
//        return avatarImageUrl;
//    }
//
//    public void setAvatarImageUrl(String userPicUrl) {
//        this.avatarImageUrl = userPicUrl;
//    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
