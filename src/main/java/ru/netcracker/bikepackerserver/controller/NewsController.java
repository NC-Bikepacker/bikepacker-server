package ru.netcracker.bikepackerserver.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.model.NewsModel;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.NewsRepo;
import ru.netcracker.bikepackerserver.service.NewsServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/news")
@Validated
@Api(tags = {"News controller: creating and getting news"})
public class NewsController {

    private NewsRepo newsRepo;
    private NewsServiceImpl newsService;

    @Autowired
    public NewsController(NewsRepo newsRepo, NewsServiceImpl newsService) {
        this.newsRepo = newsRepo;
        this.newsService = newsService;
    }

    @GetMapping("/getnews/{userid}")
    @ApiOperation(value = "get news", notes = "This request getting news all the user's friends")
    public ResponseEntity getOneTrack(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "user id",
                    example = "13",
                    required = true
            )
            @PathVariable @Valid Long userid
    ) {
        if (userid != null) {
            return new ResponseEntity(newsService.getNewsMyFriends(userid), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create news", notes = "This request creates a new news")
    public ResponseEntity createNews(
            @RequestBody
            @ApiParam(
                    name = "News Model",
                    type = "NewsModel",
                    value = "NewsModel",
                    required = true
            )
                    NewsModel news
    ) throws Exception {
        newsService.save(news);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteNews(@PathVariable(name = "id") Long id){
        try {
            newsService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update a news data", notes = "This request changes current news")
    public ResponseEntity updateNews(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "134",
                    example = "134",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody NewsModel newsModel

    ) {
        newsService.update(newsModel);
        return new ResponseEntity(HttpStatus.OK);
    }

}
