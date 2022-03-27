package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.exception.NullPointModelException;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.PointRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private final PointRepo pointRepo;

    @Autowired
    private final TrackRepo trackRepo;

    @Autowired
    private final ImageServiceImpl imageService;

    public PointServiceImpl(PointRepo pointRepo, TrackRepo trackRepo, ImageRepo imageRepo, ImageServiceImpl imageService) {
        this.pointRepo = pointRepo;
        this.trackRepo = trackRepo;
        this.imageService = imageService;
    }

    @Override
    public void save(PointModel pointModel) throws NullPointModelException {
        PointEntity pointEntity = PointModel.toEntity(pointModel, trackRepo).orElseThrow(NullPointModelException::new);
        PointEntity point = pointRepo.save(pointEntity);
        List<ImageEntity> imageEntities = ImageEntity.toEntity(pointModel, point);
        imageService.saveAll(imageEntities);
    }

    @Override
    public void saveAll(List<PointModel> models) throws NullPointModelException {
        int size = models.size();
        List<PointEntity> pointEntities = new ArrayList<>(size);
        List<ImageEntity> imageEntities = new ArrayList<>();

        for (PointModel pointModel : models) {
            pointEntities.add(PointModel.toEntity(pointModel, trackRepo).orElseThrow(NullPointModelException::new));
        }

        List<PointEntity> savedPointEntities = pointRepo.saveAll(pointEntities);

        for (int i = 0; i < size; i++) {
            imageEntities.addAll(ImageEntity.toEntity(models.get(i), savedPointEntities.get(i)));
        }

        imageService.saveAll(imageEntities);
    }
}
