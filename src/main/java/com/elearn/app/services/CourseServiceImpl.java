package com.elearn.app.services;

import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.CourseDto;
import com.elearn.app.dto.ResourceContentType;
import com.elearn.app.entities.Course;
import com.elearn.app.entities.Video;
import com.elearn.app.exception.ResourceNotFoundException;
import com.elearn.app.repositories.CourseRepo;
import com.elearn.app.repositories.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepo courseRepo;

    private ModelMapper modelMapper;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private FileService fileService;

    //Auto Dependency Injection will happened..
    public CourseServiceImpl(CourseRepo courseRepo, ModelMapper modelMapper) {
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {

      courseDto.setId(UUID.randomUUID().toString());
      courseDto.setCreatedDate(new Date());
      Course savedCourse =   courseRepo.save(dtoToEntity(courseDto));
      return entityToDto(savedCourse);

    }


    @Override
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepo.findAll(pageable);
        List<CourseDto> courseDtoList =  courses.getContent().stream().map(course -> entityToDto(course)).collect(Collectors.toList());

        //converting each object of course into the courseDto object

        //List<CourseDto> courseDtoList =  courses.stream().map(course->entityToDto(course)).collect(Collectors.toList());

        return new PageImpl<>(courseDtoList,pageable,courses.getTotalElements());
    }


    @Override
    public CourseDto updateCourse(String courseId, CourseDto courseDto) {

       Course course = courseRepo.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course Not Found!!"));
       Course updatedCourse = courseRepo.save(course);

       return entityToDto(updatedCourse);
    }

    @Override
    public CourseDto getCourseByID(String id) {

        Course course = courseRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Course Not found!!!"));
        return entityToDto(course);
    }

    @Override
    public void deleteCourse(String courseId) {

       courseRepo.deleteById(courseId);
    }

    @Override
    public List<CourseDto> searchCourses(String keyword) {

       List<Course> courses =  courseRepo.findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(keyword,keyword);
       return courses.stream().map(course -> entityToDto(course)).collect(Collectors.toList());

    }

    @Override
    public CourseDto courseBanner(MultipartFile file, String courseId) throws IOException {

       Course course =  courseRepo.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course not found!!"));
       String filePath =   fileService.save(file, AppConstants.COURSE_BANNER_UPLOAD_DIR,file.getOriginalFilename());
       course.setBanner(filePath);
       course.setBannerContentType(file.getContentType());
       Course updatedCourse = courseRepo.save(course);
       return entityToDto(updatedCourse);
    }

    @Override
    public ResourceContentType getCourseBannerById(String courseId) {

      Course course =   courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course Not Found!!"));
      String bannerPath =  course.getBanner();  //this will retrive the banner path location which we have saved
      Path path =   Paths.get(bannerPath);
      Resource resource= new FileSystemResource(path);
      ResourceContentType resourceContentType = new ResourceContentType();
      resourceContentType.setResource(resource);
      resourceContentType.setContentType(course.getBannerContentType());
      return resourceContentType;
    }

    @Override
    @Transactional
    public void addVideoToCourse(String courseId, String videoId) {

     Course course =   courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course Not Found"));

     Video video   =   videoRepo.findById(videoId).orElseThrow(()->new ResourceNotFoundException("Video Not Found.."));

     course.addVideo(video);

     courseRepo.save(course);

     System.out.println("Course And Video Relationship Updated!!!!");

    }

    public CourseDto entityToDto(Course course){

       CourseDto courseDto =  modelMapper.map(course,CourseDto.class);
       return courseDto;
    }

    public Course dtoToEntity(CourseDto courseDto){

       Course course= modelMapper.map(courseDto,Course.class);
       return course;
    }
}
