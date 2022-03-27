package ru.netcracker.bikepackerserver.entity;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.model.ImageModel;
import ru.netcracker.bikepackerserver.model.PointModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "image", schema = "public")
@Validated
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "image_id")
    @NotNull
    private Long imageId;

    @JoinColumn(name = "image_base64")
    @NotNull
    private String imageBase64;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Nullable
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id")
    @Nullable
    private TrackEntity trackId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "point_id")
    @Nullable
    private PointEntity point;

    public Long getImageId() {
        return imageId;
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
    public PointEntity getPoint() {
        return point;
    }

    public void setPoint(@Nullable PointEntity pointId) {
        this.point = pointId;
    }

    public static ImageEntity toEntity(ImageModel model) {
        ImageEntity entity = null;

        if (model != null && model.getImageBase64() != null) {
            entity = new ImageEntity();
            entity.setImageBase64(model.getImageBase64());
            entity.setTrackId(model.getTrackId());
            entity.setPoint(model.getPointId());
            entity.setUserId(model.getUserId());
        }

        return entity;
    }

    public static List<ImageEntity> toEntity(PointModel pointModel, PointEntity pointEntity) {
        List<ImageEntity> imageEntities = new ArrayList<>();

        if (pointModel != null) {
            List<String> images = pointModel.getImages();

            if (images != null && images.size() != 0) {
                if (pointEntity != null) {
                    for (String image : images) {
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setPoint(pointEntity);
                        imageEntity.setImageBase64(image);
                        imageEntities.add(imageEntity);
                    }
                }
            }
        }

        return imageEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageEntity)) return false;
        ImageEntity that = (ImageEntity) o;
        return Objects.equals(getImageId(), that.getImageId()) && Objects.equals(getImageBase64(), that.getImageBase64()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getTrackId(), that.getTrackId()) && Objects.equals(getPoint(), that.getPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageId(), getImageBase64(), getUserId(), getTrackId(), getPoint());
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "imageId=" + imageId +
                ", userId=" + userId +
                ", trackId=" + trackId +
                ", pointId=" + point +
                '}';
    }
}
