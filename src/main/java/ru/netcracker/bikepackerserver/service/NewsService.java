package ru.netcracker.bikepackerserver.service;
import ru.netcracker.bikepackerserver.model.NewsModel;
import ru.netcracker.bikepackerserver.model.TrackModel;

import java.util.List;

public interface NewsService {
    void save(NewsModel newsModel);
    void delete(Long id);
    void update(NewsModel newsModel);
    NewsModel getOneNews(Long id);
    List<NewsModel> getAllNews();
    List<NewsModel> getNewsMyFriends(Long userId);
}
