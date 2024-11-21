package com.elearn.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    private String id;

    private String title;

    private String shortDesc;

    @Column(length = 2000)
    private String longDesc;

    private double price;

    private boolean live=false;

    private double discount;

    private Date createdDate;

    private String banner; //here we will store the banner name... will store the file banner/photo at folder structure..

    private String bannerContentType;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL

    )
    private List<Video> videos = new ArrayList<>();

    @ManyToMany
    private List<Category> categories = new ArrayList<>();


    public void addVideo(Video video){

        //video list main video add kiya
        videos.add(video);

        video.setCourse(this);
    }

    public void addCategory(Category category){

        //category list main category add kiya
        categories.add(category);

        //category ke course list main current course add kiya
        category.getCourses().add(this);
    }


    public void removeCategory(Category category){

        categories.remove(category);

        category.getCourses().remove(this);
    }
}
