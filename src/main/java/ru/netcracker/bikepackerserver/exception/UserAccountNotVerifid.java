package ru.netcracker.bikepackerserver.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAccountNotVerifid extends AuthenticationException {
    public UserAccountNotVerifid(String msg) {
        super(msg);
    }
}
