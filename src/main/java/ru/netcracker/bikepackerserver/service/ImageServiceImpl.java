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
        if (pointId != null) {
            Optional<PointEntity> point = Optional.ofNullable(pointRepo.findPointEntityById(pointId));

            if (point.isPresent()) {
                List<ImageEntity> images = imageRepo.findImageEntitiesByPoint(point.get());
                return images;
            } else {
                LoggerFactory.getLogger(PointModel.class).error("There is no point with id " + pointId);
                throw new NullPointModelException();
            }
        } else {
            LoggerFactory.getLogger(PointModel.class).error("Point id can't be null. ");
            throw new NullPointIdException();
        }
    }

    @Override
    public List<ImageModel> getImageModelsByPointId(Long pointId) throws BaseException {
        if (pointId != null) {
            Optional<PointEntity> point = Optional.ofNullable(pointRepo.findPointEntityById(pointId));

            if (point.isPresent()) {
                List<ImageEntity> imageEntities = imageRepo.findImageEntitiesByPoint(point.get());

                List<ImageModel> imageModels = new ArrayList<>(imageEntities.size());

                for (ImageEntity entity : imageEntities) {
                    imageModels.add(ImageModel.toModel(entity).orElseThrow(NullPointModelException::new));
                }

                return imageModels;
            } else {
                LoggerFactory.getLogger(PointModel.class).error("There is no point with id " + pointId);
                throw new NullPointModelException();
            }
        } else {
            LoggerFactory.getLogger(PointModel.class).error("Point id can't be null. ");
            throw new NullPointIdException();
        }
    }

    @Override
    public List<String> getImagesBase64ByPointId(Long pointId) throws BaseException {
        if (pointId != null) {
            List<ImageModel> imageModels = getImageModelsByPointId(pointId);
            List<String> imagesBase64 = new ArrayList<>(imageModels.size());

            for (ImageModel image : imageModels) {
                imagesBase64.add(image.getImageBase64());
            }

            return imagesBase64;
        } else {
            LoggerFactory.getLogger(PointModel.class).error("Point id can't be null. ");
            throw new NullPointIdException();
        }
    }
}
