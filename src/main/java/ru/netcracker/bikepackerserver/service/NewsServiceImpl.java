package ru.netcracker.bikepackerserver.service;

import org.apache.commons.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.NewsEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.ImageNotFoundException;
import ru.netcracker.bikepackerserver.exception.NewsNotFoundException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.ImageModel;
import ru.netcracker.bikepackerserver.model.NewsModel;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.NewsRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService{
    @Autowired
    private NewsRepo newsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private TrackRepo trackRepo;
    @Autowired
    private TrackImageService trackImageService;
    @Autowired
    private FriendService friendService;

    @Override
    public void save(NewsModel newsModel) {
        if(newsModel!=null){
            newsRepo.save(NewsEntity.toEntity(newsModel,userRepo,trackRepo));
            if(newsModel.getImages() != null && !newsModel.getImages().isEmpty()){
                List<ImageEntity> imageEntities = ImageEntity.toEntities(newsModel.getImages(),userRepo, trackRepo);
                imageEntities.stream().forEach((p)->imageRepo.save(p));
            }
        }
        else {
            LoggerFactory.getLogger(NewsServiceImpl.class).info("Dont save news. News is null");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<NewsEntity> news = Optional.ofNullable(newsRepo.findById(id).orElseThrow(() -> new NewsNotFoundException(id)));
        if(news.isPresent()){
            newsRepo.delete(news.get());
        }
    }

    @Override
    public void update(NewsModel newsModel) {
        Optional<NewsEntity> news = Optional.ofNullable(newsRepo.findById(newsModel.getId()).orElseThrow(() -> new NewsNotFoundException(newsModel.getId())));
        if(news.isPresent()){
            newsRepo.save(NewsEntity.toEntity(newsModel,userRepo,trackRepo));
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public NewsModel getOneNews(Long id) {
        NewsEntity news = newsRepo.findById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return NewsModel.toModel(news,imageRepo,trackImageService);
    }

    @Override
    public List<NewsModel> getAllNews() {
        List<NewsEntity> newsEntities = newsRepo.findAll();
        return newsEntities.stream().map(p->NewsModel.toModel(p,imageRepo,trackImageService)).collect(Collectors.toList());
    }

    @Override
    public List<NewsModel> getNewsMyFriends(Long userId) {
        List<UserEntity> friends = friendService.getFriends(userId);
        List<NewsEntity> newsEtities = new ArrayList<>();
        friends.stream()
                .map(p -> newsRepo.findByUser(p))
                .filter(Objects::nonNull)
                .forEach(p->newsEtities.addAll(p));

        return newsEtities.stream()
                .map((p)->NewsModel.toModel(p,imageRepo,trackImageService))
                .collect(Collectors.toList());
    }
}
