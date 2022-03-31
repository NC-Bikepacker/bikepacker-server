package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.model.PointModel;

import java.util.List;

public interface PointService {

    void save(PointModel pointModel);

    void saveAll(List<PointModel> models);

    PointModel getPointModelById(Long pointId);

    PointEntity getPointEntityById(Long pointId);

    List<PointModel> getPointModelsByTrackId(Long trackId) throws Exception;

    List<PointModel> getPointModelsByCoordinates(double latitudeStart, double latitudeEnd, double longitudeStart, double longitudeEnd);

    List<PointModel> getPointModelsByDescription(String text);
}
