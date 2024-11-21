package com.elearn.app.controllers;


import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.CourseDto;
import com.elearn.app.dto.CustomMessage;
import com.elearn.app.dto.ResourceContentType;
import com.elearn.app.services.CourseService;
import com.elearn.app.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(
            @RequestBody CourseDto courseDto){

       CourseDto courseDto1 = courseService.createCourse(courseDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(courseDto1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable  String courseId,
            @RequestBody   CourseDto courseDto){

       return ResponseEntity.ok(courseService.updateCourse(courseId,courseDto));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(
            @PathVariable String courseId){

        return ResponseEntity.ok(courseService.getCourseByID(courseId));

    }

    @GetMapping
    public ResponseEntity<Page<CourseDto>> getAllCourses(Pageable pageable){

        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId){

        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam String keyword){
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    @PostMapping("/{courseId}/videos/{videoId}")
    public ResponseEntity<CustomMessage> addVideoToCourse(
            @PathVariable String courseId,
            @PathVariable String videoId
    ){

        courseService.addVideoToCourse(courseId,videoId);

        CustomMessage customMessage = new CustomMessage();
        customMessage.setSuccess(true);
        customMessage.setMessage("Course Updated!!");
        return ResponseEntity.ok(customMessage);
    }

    //@Autowired
    //private FileService fileService;

    //here inside the ResponseEntity we have pass ?
    //reason that two different types of data returning hence we have write/pass ?
    @RequestMapping("/{courseId}/banners")
    public ResponseEntity<?> uploadBanner(
            @PathVariable String courseId,
            @RequestParam("banner") MultipartFile banner
            ) throws IOException {

        //here we are applying the validation where we are only accepting below mediatype
        // as if there is no validation then video also gets uploaded.
        String contentType = banner.getContentType();

        if(contentType==null){

            //setting default value if there is null
            contentType = "image/png";
        }else if(contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpeg")){

        }else{

            CustomMessage customMessage = new CustomMessage();
            customMessage.setSuccess(false);
            customMessage.setMessage("Invalid File");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customMessage);
        }

        System.out.println(banner.getOriginalFilename());
        System.out.println(banner.getName());
        System.out.println(banner.getSize());
        System.out.println(banner.getContentType());

        //fileService.save(banner, AppConstants.COURSE_BANNER_UPLOAD_DIR,banner.getOriginalFilename());
        CourseDto courseDto =  courseService.courseBanner(banner,courseId);
        return ResponseEntity.ok(courseDto);
    }

    //to access the file stored at given location
    //Resource--> from this we have used--->org.Springframwork.core.io

    @GetMapping("/{courseId}/banners")
    public ResponseEntity<Resource> serveBanner(
            @PathVariable String courseId,
            HttpServletRequest request
    ){

        //to get the header information of request we have used the HttpServletRequest obj
       Enumeration<String> headerNames = request.getHeaderNames();
       while(headerNames.hasMoreElements()){

           String header = headerNames.nextElement();
           System.out.println(header+ " :"+ request.getHeader(header) );
       }
       ResourceContentType resourceContentType =  courseService.getCourseBannerById(courseId);

       return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(resourceContentType.getContentType()))
               .body(resourceContentType.getResource());
    }
}
