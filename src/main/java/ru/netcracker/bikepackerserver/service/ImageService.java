package ru.netcracker.bikepackerserver.service;

import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.model.ImageModel;

import java.util.List;

@Service
public interface ImageService {
    void save(ImageEntity imageEntity);

    void saveAll(List<ImageEntity> images);

    List<ImageEntity> getImageEntitiesByPointId(Long pointId);

    List<ImageModel> getImageModelsByPointId(Long pointId);

    List<String> getImagesBase64ByPointId(Long pointId);
}
