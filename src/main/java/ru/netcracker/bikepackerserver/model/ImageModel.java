package ru.netcracker.bikepackerserver.model;

import com.sun.istack.Nullable;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.exception.BaseException;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Optional;

public class ImageModel {

    @NotEmpty(message = "Image may not be empty")
    private Long imageId;

    @NotEmpty(message = "Image may not be empty")
    private String imageBase64;

    @Nullable
    private UserModel user;

    @Nullable
    private TrackModel track;

    @Nullable
    private PointEntity point;

    public ImageModel() {
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public TrackModel getTrack() {
        return track;
    }

    public void setTrack(TrackModel track) {
        this.track = track;
    }

    public PointEntity getPoint() {
        return point;
    }

    public void setPoint(PointEntity point) {
        this.point = point;
    }

    public static Optional<ImageModel> toModel(ImageEntity imageEntity, ImageRepo imageRepo, TrackImageService trackImageService) throws BaseException {
        ImageModel imageModel = new ImageModel();
        Optional<ImageEntity> entity = Optional.ofNullable(imageEntity);
        if (entity.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (imageEntity.getImageId() != null) {
            imageModel.setImageId(imageEntity.getImageId());
        }
        imageModel.setUser(UserModel.toModel(imageEntity.getUser()));
        imageModel.setTrack(TrackModel.toModel(imageEntity.getTrack(), imageRepo, trackImageService));
        imageModel.setImageBase64(imageEntity.getImageBase64());
        imageModel.setPoint(imageEntity.getPoint());

        return Optional.ofNullable(imageModel);
    }
}
