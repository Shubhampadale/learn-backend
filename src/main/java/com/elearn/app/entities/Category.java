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
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description")
    private String desc;

    private Date addedDate;

    @ManyToMany(
            mappedBy = "categories",
            cascade = CascadeType.ALL
    )
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course){

        //courses list main course ko add kiya
        courses.add(course);

        //course ki category list main current category ko add kiya.
        course.getCategories().add(this);
    }

    //same to be done with removal process
    public void removeCourse(Course course){

        courses.remove(course);

        course.getCategories().remove(this);
    }
}
