package ru.netcracker.bikepackerserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import ru.netcracker.bikepackerserver.model.NewsModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "news_card", schema = "public")
public class NewsEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "track_id")
    private TrackEntity track;

    private String description;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name="\"like\"")
    private Long like;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(TrackEntity track) {
        this.track = track;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public static NewsEntity toEntity(NewsModel newsModel, UserRepo userRepo, TrackRepo trackRepo){
        NewsEntity entity = new NewsEntity();

        if(newsModel!=null){
            if(newsModel.getId()!=null){entity.setId(newsModel.getId());}
            if(newsModel.getTrack()!=null){entity.setTrack(trackRepo.findById(newsModel.getTrack().getTrackId()).get());}
            if(!newsModel.getDescription().isBlank()){ entity.setDescription(newsModel.getDescription());}
            if(newsModel.getDate()!=null){ entity.setDate(newsModel.getDate());}
            if(newsModel.getUser()!=null){entity.setUser(userRepo.findByid(newsModel.getUser().getId()));}
            if(newsModel.getLike()!=null){ entity.setLike(newsModel.getLike());}
        }
        return entity;
    }

    @Override
    public String toString() {
        return "NewsEntity{" +
                "id=" + id +
                ", track=" + track +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", like=" + like +
                '}';
    }
}
