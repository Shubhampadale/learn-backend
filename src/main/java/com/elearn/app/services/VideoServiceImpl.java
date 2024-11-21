package com.elearn.app.services;

import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.ResourceContentType;
import com.elearn.app.dto.VideoDto;
import com.elearn.app.entities.Video;
import com.elearn.app.exception.ResourceNotFoundException;
import com.elearn.app.repositories.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public VideoDto createVideo(VideoDto videoDto) {

       videoDto.setVideoId(UUID.randomUUID().toString());
       Video video =  modelMapper.map(videoDto, Video.class);
       Video savedVideo =   videoRepo.save(video);
        return modelMapper.map(savedVideo,VideoDto.class);
    }

    @Override
    public VideoDto updateVideo(String videoId, VideoDto videoDto) {

        Video video =  videoRepo.findById(videoId).orElseThrow(()->new ResourceNotFoundException("Video not found!!"));
        modelMapper.map(videoDto,video);
        Video updatedVideo =  videoRepo.save(video);
        return modelMapper.map(updatedVideo,VideoDto.class);
    }

    @Override
    public VideoDto getVideoById(String videoId) {

        Video video =  videoRepo.findById(videoId).orElseThrow(()->new ResourceNotFoundException("Video not found!!"));

        return modelMapper.map(video,VideoDto.class);
    }

    @Override
    public Page<VideoDto> getAllVideos(Pageable pageable) {

        Page<Video> videos = videoRepo.findAll(pageable);
        List<VideoDto> videoDtoList = videos.getContent().stream().map(v-> modelMapper.map(v,VideoDto.class)).collect(Collectors.toList());
        return new PageImpl<>(videoDtoList,pageable,videos.getTotalElements());
    }

    @Override
    public void deleteVideo(String videoId) {

        videoRepo.deleteById(videoId);
    }

    @Override
    public List<VideoDto> searchVideos(String keyword) {

        List<Video> videos = videoRepo.findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(keyword,keyword);

        return videos.stream().map(v-> modelMapper.map(v,VideoDto.class)).collect(Collectors.toList());
    }

    @Autowired
    private FileService fileService;
    @Override
    public VideoDto videoBanner(String videoId, MultipartFile file) throws IOException {

       Video video = videoRepo.findById(videoId).orElseThrow(()->new ResourceNotFoundException("Video Not Found!!"));
       String videoFilePath =  fileService.save(file, AppConstants.VIDEO_BANNER_UPLOAD_DIR,file.getOriginalFilename());
       video.setFilePath(videoFilePath);
       video.setContentType(file.getContentType());
       Video updatedVideo =  videoRepo.save(video);
       return modelMapper.map(updatedVideo, VideoDto.class);

    }

    @Override
    public ResourceContentType getVideo(String videoId) {

        Video video =   videoRepo.findById(videoId).orElseThrow(()->new ResourceNotFoundException("Video Not Found!!!"));
        String videoPath =  video.getFilePath();
        Path path = Paths.get(videoPath);
        Resource resource = new FileSystemResource(path);
        ResourceContentType resourceContentType = new ResourceContentType();
        resourceContentType.setResource(resource);
        resourceContentType.setContentType(video.getContentType());
        return resourceContentType;
    }


}
