package ru.netcracker.bikepackerserver.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@Validated
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "Firstname can not be empty")
    private String firstname;

    @Column(name = "last_name")
    @NotEmpty(message = "Lastname can not be empty")
    private String lastname;

    @Column(name = "nickname")
    @NotEmpty(message = "Username can not be empty")
    @Size(min = 4, max = 20, message = "Username length should be between 4 an 20 characters")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password can not be empty")
    private String password;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    @NotNull(message = "Roles can not be null")
    private RoleEntity roles;

    @Column(name = "email")
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email is not valid")
    private String email;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<TrackEntity> tracks;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public RoleEntity getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public Set<TrackEntity> getTracks() {
        return tracks;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public void setRoles(RoleEntity roles) {
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTracks(Set<TrackEntity> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getFirstname(), that.getFirstname()) && Objects.equals(getLastname(), that.getLastname()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getAvatarImageUrl(), that.getAvatarImageUrl()) && Objects.equals(getRoles(), that.getRoles()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getTracks(), that.getTracks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getUsername(), getPassword(), getAvatarImageUrl(), getRoles(), getEmail(), getTracks());
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", avatarImageUrl='" + avatarImageUrl + '\'' +
                ", roles=" + roles +
                ", email='" + email + '\'' +
                ", tracks=" + tracks +
                '}';
    }
}