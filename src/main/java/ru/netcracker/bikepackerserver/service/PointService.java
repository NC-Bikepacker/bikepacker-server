package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.exception.NullPointModelException;
import ru.netcracker.bikepackerserver.model.PointModel;

import java.util.List;

public interface PointService {

    void save(PointModel pointModel) throws NullPointModelException;

    void saveAll(List<PointModel> models) throws NullPointModelException;
}
