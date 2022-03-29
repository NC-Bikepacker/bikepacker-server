package ru.netcracker.bikepackerserver.model;

import org.springframework.lang.Nullable;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Optional;

public class ImageModel {

    @NotEmpty(message = "Image may not be empty")
    private String imageBase64;

    @Nullable
    private UserEntity user;

    @Nullable
    private TrackEntity track;

    @Nullable
    private PointEntity point;

    public ImageModel() {
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Nullable
    public UserEntity getUser() {
        return user;
    }

    public void setUser(@Nullable UserEntity user) {
        this.user = user;
    }

    @Nullable
    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(@Nullable TrackEntity track) {
        this.track = track;
    }

    @Nullable
    public PointEntity getPoint() {
        return point;
    }

    public void setPoint(@Nullable PointEntity point) {
        this.point = point;
    }

    public static Optional<ImageModel> toModel(ImageEntity imageEntity) {
        ImageModel model = null;

        if (imageEntity != null) {
            model = new ImageModel();

            model.setTrack(imageEntity.getTrackId());
            model.setPoint(imageEntity.getPoint());
            model.setUser(imageEntity.getUserId());
            model.setImageBase64(imageEntity.getImageBase64());
        }

        return Optional.ofNullable(model);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageModel)) return false;
        ImageModel that = (ImageModel) o;
        return Objects.equals(getImageBase64(), that.getImageBase64()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getTrack(), that.getTrack()) && Objects.equals(getPoint(), that.getPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageBase64(), getUser(), getTrack(), getPoint());
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "imageBase64='" + imageBase64 + '\'' +
                ", userId=" + user +
                ", trackId=" + track +
                ", pointId=" + point +
                '}';
    }
}
