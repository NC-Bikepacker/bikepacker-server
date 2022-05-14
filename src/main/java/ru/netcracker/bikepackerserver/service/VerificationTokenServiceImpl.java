package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.VerificationTokenEntity;
import ru.netcracker.bikepackerserver.repository.VerificationTokenRepo;

import javax.transaction.Transactional;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    public VerificationTokenServiceImpl(VerificationTokenRepo verificationTokenRepo) {
        this.verificationTokenRepo = verificationTokenRepo;
    }

    @Override
    public VerificationTokenEntity getVerificationToken(String verificationToken) {
        return verificationTokenRepo.findByToken(verificationToken);
    }
}
