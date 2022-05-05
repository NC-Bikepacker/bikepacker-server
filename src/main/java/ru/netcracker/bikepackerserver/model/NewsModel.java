package ru.netcracker.bikepackerserver.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.NewsEntity;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class NewsModel {

    private Long id;

    private TrackModel track;

    private String description;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private UserModel user;

    private List<ImageModel> images;

    private Long like;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackModel getTrack() {
        return track;
    }

    public void setTrack(TrackModel track) {
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public static NewsModel toModel(NewsEntity newsEntity, ImageRepo imageRepo, TrackImageService trackImageService){
        NewsModel newsModel = new NewsModel();
        List<ImageEntity> images = imageRepo.findImageEntitiesByNews(newsEntity);

        if(newsEntity!=null){
            if(newsEntity.getId()!=null){ newsModel.setId(newsEntity.getId());}
            if(newsEntity.getTrack()!=null){ newsModel.setTrack(TrackModel.toModel(newsEntity.getTrack(), imageRepo, trackImageService)); }
            if(newsEntity.getDescription()!=null){ newsModel.setDescription(newsEntity.getDescription()); }
            if(newsEntity.getDate()!=null){ newsModel.setDate(newsEntity.getDate()); }
            if(newsEntity.getUser()!=null){ newsModel.setUser(UserModel.toModel(newsEntity.getUser())); }
            if(!images.isEmpty()){ newsModel.setImages(ImageModel.toModels(images, imageRepo, trackImageService)); }
            if(newsEntity.getLike()!=null){ newsModel.setLike(newsEntity.getLike()); }
        }
    return newsModel;
    }

    public static List<NewsModel> toModels(List<NewsEntity> newsEntities, ImageRepo imageRepo, TrackImageService trackImageService){
        return newsEntities.stream().map(p->NewsModel.toModel(p, imageRepo,trackImageService)).collect(Collectors.toList());
    }
}
