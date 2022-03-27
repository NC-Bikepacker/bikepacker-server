package ru.netcracker.bikepackerserver.service;

import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;

import java.util.List;

@Service
public interface ImageService {
    void save(ImageEntity imageEntity);

    void saveAll(List<ImageEntity> images);
}
