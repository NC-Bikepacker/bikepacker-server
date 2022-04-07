package ru.netcracker.bikepackerserver.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "favorite_track", schema = "public")
@Validated
public class FavoriteTrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_track_id")
    @NotNull
    private Long favoriteTrackId;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "track_id")
    private  TrackEntity track;

    public FavoriteTrackEntity() {
    }

    public Long getFavoriteTrackId() {
        return favoriteTrackId;
    }

    public void setFavoriteTrackId(Long favoriteTrackId) {
        this.favoriteTrackId = favoriteTrackId;
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

    @Override
    public String toString() {
        return "FavoriteTrackEntity{" +
                "favoriteTrackId=" + favoriteTrackId +
                ", user=" + user +
                ", track=" + track +
                '}';
    }
}
