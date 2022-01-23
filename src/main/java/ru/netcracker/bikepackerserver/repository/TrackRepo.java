package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface TrackRepo extends JpaRepository<TrackEntity, Long> {

    List<TrackEntity> findByUser(Optional<UserEntity> user);

}
