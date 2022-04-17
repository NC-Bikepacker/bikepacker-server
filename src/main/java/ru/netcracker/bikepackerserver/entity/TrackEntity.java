package ru.netcracker.bikepackerserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.exception.NoAnyFavoriteTrackException;
import ru.netcracker.bikepackerserver.exception.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exception.NoSuchTrackException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="tracks", schema = "public")
@Validated
public class TrackEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "travel_time")
    private Long travelTime;

    @Column(name = "track_complexity")
    private short trackComplexity;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull
    private  UserEntity user;

    private String gpx;

    public TrackEntity() {
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long track_id) {
        this.trackId = track_id;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travel_time) {
        this.travelTime = travel_time;
    }

    public short getTrackComplexity() {
        return trackComplexity;
    }

    public void setTrackComplexity(short track_complexity) {
        this.trackComplexity = track_complexity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getGpx() {
        return gpx;
    }

    public void setGpx(String gpx) {
        this.gpx = gpx;
    }

    @Override
    public String toString() {
        return "TrackEntity{" +
                "track_id=" + trackId +
                ", travel_time=" + travelTime +
                ", track_complexity=" + trackComplexity +
                ", user="  + user +
                ", gpx_url='" + gpx+ '\'' +
                '}';
    }

    public static TrackEntity toEntity(TrackModel model, UserRepo userRepo) throws NoAnyFavoriteTrackException {
        Optional<TrackModel> trackModel = Optional.ofNullable(model);
        TrackEntity trackEntity = new TrackEntity();
        UserEntity userEntity;

        System.out.println("to entity");

        if (trackModel.isPresent()) {
            Optional<Long> travelTime = Optional.ofNullable(trackModel.get().getTravelTime());
            Optional<Short> trackComplexity = Optional.ofNullable(trackModel.get().getTrackComplexity());
            Optional<Long> userId = Optional.ofNullable(trackModel.get().getUser().getId());
            Optional<String> gpx = Optional.ofNullable(trackModel.get().getGpx());

            System.out.println("trackmodel is present" + trackModel.toString());

            if (travelTime.isPresent()){
                trackEntity.setTravelTime(trackModel.get().getTravelTime());
                System.out.println("traveltime is present" + travelTime.toString());
            } else {
                throw new NoSuchTrackException();
            }
            if (trackComplexity.isPresent()) {
                trackEntity.setTrackComplexity(trackModel.get().getTrackComplexity());
                System.out.println("trackcomplexity is present");
            } else {
                throw new NoSuchTrackException();
            }
            if (gpx.isPresent()) {
                trackEntity.setGpx(trackModel.get().getGpx());
                System.out.println("gpx is present");
            } else {
                throw new NoSuchTrackException();
            }
            if (userId.isPresent()) {
                userEntity = userRepo.findByid(userId.get());
                System.out.println("userId is present");
                if(userEntity!=null){
                    trackEntity.setUser(userEntity);
                    System.out.println("userEntity != null");
                }
                else {
                    throw new NoAnyUsersException();
                }
            } else {
                throw new NoAnyUsersException();
            }
            System.out.println("return trackEntity");
            return trackEntity;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}