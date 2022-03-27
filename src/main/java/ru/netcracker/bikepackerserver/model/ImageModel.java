package ru.netcracker.bikepackerserver.model;

import org.springframework.lang.Nullable;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class ImageModel {

    @NotEmpty(message = "Image may not be empty")
    private String imageBase64;

    @Nullable
    private UserEntity userId;

    @Nullable
    private TrackEntity trackId;

    @Nullable
    private PointEntity pointId;

    public ImageModel() {
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Nullable
    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UserEntity userId) {
        this.userId = userId;
    }

    @Nullable
    public TrackEntity getTrackId() {
        return trackId;
    }

    public void setTrackId(@Nullable TrackEntity trackId) {
        this.trackId = trackId;
    }

    @Nullable
    public PointEntity getPointId() {
        return pointId;
    }

    public void setPointId(@Nullable PointEntity pointId) {
        this.pointId = pointId;
    }

    public static ImageEntity toEntity(ImageModel imageModel) {
        ImageEntity imageEntity = new ImageEntity();

        imageEntity.setUserId(imageModel.getUserId());
        imageEntity.setTrackId(imageModel.getTrackId());
        imageEntity.setPoint(imageModel.getPointId());
        imageEntity.setImageBase64(imageModel.getImageBase64());

        return imageEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageModel)) return false;
        ImageModel that = (ImageModel) o;
        return Objects.equals(getImageBase64(), that.getImageBase64()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getTrackId(), that.getTrackId()) && Objects.equals(getPointId(), that.getPointId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageBase64(), getUserId(), getTrackId(), getPointId());
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "imageBase64='" + imageBase64 + '\'' +
                ", userId=" + userId +
                ", trackId=" + trackId +
                ", pointId=" + pointId +
                '}';
    }
}
