package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.VerificationTokenEntity;

public interface VerificationTokenService {

    VerificationTokenEntity getVerificationToken(String verificationToken);


}
