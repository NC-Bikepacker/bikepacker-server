package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;

@Repository
public interface TrackRepo extends JpaRepository<TrackEntity, Long> {
    List<TrackEntity> findByUser (UserEntity user);
    TrackEntity findTrackEntityByTrackId(Long trackId);
}
