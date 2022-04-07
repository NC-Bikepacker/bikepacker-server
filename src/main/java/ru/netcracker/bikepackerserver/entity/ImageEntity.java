package ru.netcracker.bikepackerserver.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.model.ImageModel;

import javax.persistence.*;

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

    public static ImageEntity toEntity(ImageModel model) {
        ImageEntity entity = null;

        if (model != null && model.getImageBase64() != null) {
            entity = new ImageEntity();
            entity.setImageBase64(model.getImageBase64());
            entity.setTrack(model.getTrack());
            entity.setUser(model.getUser());
        }

        return entity;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "imageId=" + imageId +
                ", userId=" + user +
                ", trackId=" + track +
                '}';
    }
}