package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.model.PointModel;

import java.util.List;
import java.util.Map;

public interface PointService {

    void save(PointModel model, Long trackId);

    void save(Map<PointModel, Long> pointModels);

    List<PointEntity> readAll();
}
