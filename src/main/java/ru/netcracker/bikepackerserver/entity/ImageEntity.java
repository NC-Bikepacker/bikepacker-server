package ru.netcracker.bikepackerserver.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.model.ImageModel;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "track_id")
    @Nullable
    private TrackEntity track;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "point_id")
    @Nullable
    private PointEntity point;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id")
    @Nullable
    private NewsEntity news;

    public ImageEntity() {
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(TrackEntity track) {
        this.track = track;
    }

    public NewsEntity getNews() { return news;  }

    public void setNews(NewsEntity news) { this.news = news; }


    @Nullable
    public PointEntity getPoint() {
        return point;
    }

    public void setPoint(@Nullable PointEntity pointId) {
        this.point = pointId;
    }

    public static ImageEntity toEntity(ImageModel model, UserRepo userRepo, TrackRepo trackRepo) {
        ImageEntity entity = new ImageEntity();

        if (model != null && model.getImageBase64() != null) {
            entity = new ImageEntity();
            entity.setImageBase64(model.getImageBase64());
            entity.setTrack(trackRepo.getById(model.getTrack().getTrackId()));
            entity.setUser(userRepo.findByid(model.getUser().getId()));
            entity.setPoint(model.getPoint());
        }
        else {
            throw new IllegalArgumentException();
        }

        return entity;
    }

    public static List<ImageEntity> toEntities(List<ImageModel> images, UserRepo userRepo, TrackRepo trackRepo){
        if(!images.isEmpty()){
            return images.stream()
                    .filter(Objects::nonNull)
                    .map(p->ImageEntity.toEntity(p, userRepo, trackRepo))
                    .collect(Collectors.toList());
        }
        else {
            throw new IllegalArgumentException();
        }
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
    public String toString() {
        return "ImageEntity{" +
                "imageId=" + imageId +
                ", userId=" + user +
                ", trackId=" + track +
                ", pointId=" + point +
                '}';
    }
}