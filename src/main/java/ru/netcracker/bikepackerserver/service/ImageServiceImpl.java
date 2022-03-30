package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.ImageModel;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.PointRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private final ImageRepo imageRepo;

    @Autowired
    private final PointRepo pointRepo;

    public ImageServiceImpl(ImageRepo imageRepo, PointRepo pointRepo) {
        this.imageRepo = imageRepo;
        this.pointRepo = pointRepo;
    }

    @Override
    public void save(ImageEntity image) {
        imageRepo.save(image);
    }

    @Override
    public void saveAll(List<ImageEntity> images) {
        imageRepo.saveAll(images);
    }

    @Override
    public List<ImageEntity> getImageEntitiesByPointId(Long pointId) throws BaseException {
        List<ImageEntity> images = new ArrayList<>();

        if (pointId != null) {
            Optional<PointEntity> point = Optional.ofNullable(pointRepo.findPointEntityById(pointId));

            if (point.isPresent()) {
                images = imageRepo.findImageEntitiesByPoint(point.get());
            } else {
                LoggerFactory.getLogger(PointModel.class).error("It is impossible to find images by point id. There is no point with id " + pointId);
            }
        } else {
            LoggerFactory.getLogger(PointModel.class).error("Point id can't be null. ");
            throw new NullPointIdException();
        }
        return images;
    }

    @Override
    public List<ImageModel> getImageModelsByPointId(Long pointId) throws BaseException {
        List<ImageModel> imageModels = new ArrayList<>();

        if (pointId != null) {
            Optional<PointEntity> point = Optional.ofNullable(pointRepo.findPointEntityById(pointId));

            if (point.isPresent()) {
                List<ImageEntity> imageEntities = imageRepo.findImageEntitiesByPoint(point.get());

                for (ImageEntity entity : imageEntities) {
                    imageModels.add(ImageModel.toModel(entity).orElseThrow(NoAnyPointException::new));
                }

                return imageModels;
            } else {
                LoggerFactory.getLogger(PointEntity.class).error("There is no point with id " + pointId);
                throw new NoAnyPointException();
            }
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("Point id can't be null. ");
            throw new NullPointIdException();
        }
    }

    @Override
    public List<String> getImagesBase64ByPointId(Long pointId) throws BaseException {
        List<String> imagesBase64 = new ArrayList<>();
        List<ImageModel> imageModels = getImageModelsByPointId(pointId);

        for (ImageModel image : imageModels) {
            imagesBase64.add(image.getImageBase64());
        }

        return imagesBase64;
    }
}
