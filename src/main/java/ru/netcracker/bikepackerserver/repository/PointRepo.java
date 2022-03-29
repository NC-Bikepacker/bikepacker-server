package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;

import java.util.List;

@Repository
public interface PointRepo extends JpaRepository<PointEntity, Long> {
    PointEntity findPointEntityById(Long id);

    List<PointEntity> findAll();

    List<PointEntity> findPointEntitiesByTrack(TrackEntity track);

    List<PointEntity> findPointEntitiesByLatitudeIsBetweenAndLongitudeIsBetween(double latitudeStart, double latitudeEnd, double longitudeStart, double longitudeEnd);

    List<PointEntity> findPointEntitiesByDescriptionContainsIgnoreCase(String searchingString);
}