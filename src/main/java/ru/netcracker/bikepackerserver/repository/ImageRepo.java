package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {
    ImageEntity findByUser(UserEntity user);
    ImageEntity findByTrack(TrackEntity track);
}
