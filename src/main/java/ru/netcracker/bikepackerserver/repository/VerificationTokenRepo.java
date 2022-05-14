package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.entity.VerificationTokenEntity;

import java.io.Serializable;

public interface VerificationTokenRepo  extends JpaRepository<VerificationTokenEntity, Long>, Serializable {

    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUser(UserEntity user);
}
