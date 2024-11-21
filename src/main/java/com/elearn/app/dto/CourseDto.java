package com.elearn.app.dto;

import com.elearn.app.entities.Category;
import com.elearn.app.entities.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {


    private String id;

    private String title;

    private String shortDesc;

    private String longDesc;

    private double price;

    private boolean live=false;

    private double discount;

    private Date createdDate;

    private String banner; //here we will store the banner name... will store the file banner/photo at folder structure..

    private List<VideoDto> videos = new ArrayList<>();

    private List<CategoryDto> categories = new ArrayList<>();

    public String getBannerUrl(){

        return "http://localhost:8082/api/v1/courses/"+id+"/banners";
    }

}
