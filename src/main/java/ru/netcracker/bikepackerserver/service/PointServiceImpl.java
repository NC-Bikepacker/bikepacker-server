package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.PointRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private final PointRepo pointRepo;

    @Autowired
    private final TrackRepo trackRepo;

    @Autowired
    private final ImageServiceImpl imageService;

    public PointServiceImpl(PointRepo pointRepo, TrackRepo trackRepo, ImageServiceImpl imageService) {
        this.pointRepo = pointRepo;
        this.trackRepo = trackRepo;
        this.imageService = imageService;
    }

    @Override
    public void save(PointModel pointModel) {
        try {
            PointEntity pointEntity = Optional.ofNullable(PointEntity.toEntity(pointModel, trackRepo)).orElseThrow(NoAnyPointException::new);
            PointEntity point = pointRepo.save(pointEntity);
            List<ImageEntity> imageEntities = ImageEntity.toEntity(pointModel, point);
            imageService.saveAll(imageEntities);
        } catch (Exception e) {
            LoggerFactory.getLogger(PointModel.class).error("Saving point error." + e);
            throw new ServerErrorException();
        }
    }

    @Override
    public void saveAll(List<PointModel> models) {
        int size = models.size();
        List<PointEntity> pointEntities = new ArrayList<>(size);
        List<ImageEntity> imageEntities = new ArrayList<>();

        try {
            for (PointModel pointModel : models) {
                pointEntities.add(Optional.ofNullable(PointEntity.toEntity(pointModel, trackRepo)).orElseThrow(NoAnyPointException::new));
            }

            List<PointEntity> savedPointEntities = pointRepo.saveAll(pointEntities);

            for (int i = 0; i < size; i++) {
                imageEntities.addAll(ImageEntity.toEntity(models.get(i), savedPointEntities.get(i)));
            }

            imageService.saveAll(imageEntities);
        } catch (Exception e) {
            LoggerFactory.getLogger(PointModel.class).error("Saving points error." + e);
            throw new ServerErrorException();
        }
    }

    @Override
    public List<PointModel> getPointModelsByTrackId(Long trackId) throws BaseException {
        if (trackId != null) {
            Optional<TrackEntity> track = Optional.ofNullable(trackRepo.findTrackEntityByTrackId(trackId));

            if (track.isPresent()) {
                List<PointEntity> pointEntities = pointRepo.findPointEntitiesByTrack(track.get());
                List<PointModel> pointModels = new ArrayList<>(pointEntities.size());

                if (pointEntities.size() > 0) {
                    pointModels = PointModel.toModels(pointEntities, imageService);
                } else {
                    LoggerFactory.getLogger(PointModel.class).error("There are no points for track with id " + trackId);
                }

                return pointModels;
            } else {
                LoggerFactory.getLogger(TrackEntity.class).error("There is no track with id " + trackId);
                throw new NoSuchTrackException();
            }
        } else {
            LoggerFactory.getLogger(TrackEntity.class).error("Track id can't be null. ");
            throw new NullTrackIdException();
        }
    }

    @Override
    public List<PointModel> getPointModelsByCoordinates(double latitudeStart, double latitudeEnd, double longitudeStart, double longitudeEnd) throws BaseException {
        List<PointModel> pointModels = new ArrayList<>();

        if (latitudeStart < latitudeEnd && longitudeStart < longitudeEnd) {
            List<PointEntity> pointEntities = pointRepo.findPointEntitiesByLatitudeIsBetweenAndLongitudeIsBetween(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd);
            if (pointEntities.size() > 0) {
                pointModels = PointModel.toModels(pointEntities, imageService);
            } else {
                LoggerFactory.getLogger(PointModel.class).error("There are no points with such coordinates");
            }

            return pointModels;
        } else {
            LoggerFactory.getLogger(PointModel.class).error("The coordinates are incorrect. Perhaps the value of the start coordinates is greater than the end coordinates.");
            throw new WrongCoordinatesException();
        }
    }

    @Override
    public List<PointModel> getPointModelsByDescription(String text) throws BaseException {
        List<PointModel> pointModels = new ArrayList<>();

        if (text != null) {
            List<PointEntity> pointEntities = pointRepo.findPointEntitiesByDescriptionContainsIgnoreCase(text);

            if (pointEntities.size() > 0) {
                pointModels = PointModel.toModels(pointEntities, imageService);
            } else {
                LoggerFactory.getLogger(PointModel.class).error("There are no points with such a \"" + text + "\" description.");
            }

            return pointModels;
        } else {
            LoggerFactory.getLogger(PointModel.class).error("Text can't be null");
            throw new NullTextException();
        }
    }

    @Override
    public PointModel getPointModelById(Long pointId) throws BaseException {
        if (pointId != null) {
            Optional<PointEntity> pointEntity = ofNullable(pointRepo.findPointEntityById(pointId));

            if (pointEntity.isPresent()) {
                return PointModel.toModel(pointEntity.get(), imageService);
            } else {
                LoggerFactory.getLogger(PointModel.class).error("There is no point with id " + pointId);
                throw new NoAnyPointException();
            }
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("Point id can't be null");
            throw new NullPointIdException();
        }
    }

    @Override
    public PointEntity getPointEntityById(Long pointId) throws BaseException {
        if (pointId != null) {
            Optional<PointEntity> pointEntity = ofNullable(pointRepo.findPointEntityById(pointId));

            if (pointEntity.isPresent()) {
                return pointEntity.get();
            } else {
                LoggerFactory.getLogger(PointEntity.class).error("There is no point with id " + pointId);
                throw new NoAnyPointException();
            }
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("Point id can't be null");
            throw new NullPointIdException();
        }
    }
}
