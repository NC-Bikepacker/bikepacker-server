package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.PointRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PointServiceImpl implements PointService {

    @Autowired
    private final PointRepo pointRepo;
    @Autowired
    private final TrackRepo trackRepo;

    public PointServiceImpl(PointRepo pointRepo, TrackRepo trackRepo) {
        this.pointRepo = pointRepo;
        this.trackRepo = trackRepo;
    }

    @Override
    public void save(PointModel model, Long trackId) {
        PointEntity entity = PointModel.toEntity(model);
        TrackEntity track = trackRepo.getByTrackId(trackId);
        entity.setTrack(track);

        pointRepo.save(entity);
    }

    @Override
    public void save(Map<PointModel, Long> pointModels) {
        for (Map.Entry<PointModel, Long> entry : pointModels.entrySet()) {
            PointEntity entity = PointModel.toEntity(entry.getKey());
            TrackEntity track = trackRepo.getById(entry.getValue());
            entity.setTrack(track);

            pointRepo.save(entity);
        }
    }

    public List<PointEntity> readAll() {
        return pointRepo.findAll();
    }
}
