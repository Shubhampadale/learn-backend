package com.elearn.app.controllers;

import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.ResourceContentType;
import com.elearn.app.dto.VideoDto;
import com.elearn.app.entities.Video;
import com.elearn.app.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {


    //@Autowired
    private VideoService videoService;

    public VideoController(VideoService videoService){

      this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<VideoDto> createVideo(
            @RequestBody VideoDto videoDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.createVideo(videoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> updateVideo(
            @PathVariable String id,
            @RequestBody  VideoDto videoDto
    ){

        return ResponseEntity.ok(videoService.updateVideo(id,videoDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideoById(
            @PathVariable String id
    ){

        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @GetMapping
    public ResponseEntity<Page<VideoDto>> getAllVideos(Pageable pageable){

        return ResponseEntity.ok(videoService.getAllVideos(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable  String id){

        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideos(@RequestParam String keyword){

        return ResponseEntity.ok(videoService.searchVideos(keyword));
    }

    @PostMapping("/{videoId}/banners")
    public ResponseEntity<VideoDto> getVideoBanner(
            @PathVariable String videoId,
            @RequestParam("banner") MultipartFile file

    ) throws IOException {

        //based on contentType validation to be applied as only video to be get upload
        VideoDto videoDto = videoService.videoBanner(videoId,file);
        return ResponseEntity.ok().body(videoDto);
    }

    @GetMapping("/{videoId}/banners")

    public ResponseEntity<Resource> serveVideoBanner(
            @PathVariable String videoId
    ){
        ResourceContentType resourceContentType =  videoService.getVideo(videoId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceContentType.getContentType()))
                .body(resourceContentType.getResource());
    }
}
