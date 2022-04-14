package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findImageEntitiesByPoint(PointEntity pointEntity);
    ImageEntity findByUser(UserEntity user);
    ImageEntity findByTrack(TrackEntity track);
}