package ru.netcracker.bikepackerserver.service;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netcracker.bikepackerserver.entity.RoleEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;
    private String nickname;
    private String password;
    private String avatarImageUrl;
    private String email;
    private RoleEntity roles;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(UserEntity user) {
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.nickname = user.getUsername();
        this.password = user.getPassword();
        this.avatarImageUrl = user.getAvatarImageUrl();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.authorities = Arrays.stream(user.getRoles().getRole_name().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public UserDetailsImpl() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
