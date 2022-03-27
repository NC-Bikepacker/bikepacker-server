package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.repository.ImageRepo;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private final ImageRepo imageRepo;

    public ImageServiceImpl(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Override
    public void save(ImageEntity image) {
        imageRepo.save(image);
    }

    @Override
    public void saveAll(List<ImageEntity> images) {
        imageRepo.saveAll(images);
    }
}
