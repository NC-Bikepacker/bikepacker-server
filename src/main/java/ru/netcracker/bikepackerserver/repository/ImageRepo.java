package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.*;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findImageEntitiesByPoint(PointEntity pointEntity);
    List<ImageEntity> findImageEntitiesByNews(NewsEntity newsEntity);
    ImageEntity findByUser(UserEntity user);
    ImageEntity findByTrack(TrackEntity track);
}