package com.elearn.app.dto;

import com.elearn.app.entities.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private String id;


    @NotEmpty(message = "Title is required!!")
    @Size(min = 3,max = 50, message = "Length of title required between 3 & 50 Characters!!")
    private String title;

    @NotEmpty(message = "description is required!!")
    private String desc;

    private Date addedDate;

    //below will be commented as we not required whenever category fetched/get
    //@ManyToMany(mappedBy = "categories")
    //private List<Course> courses = new ArrayList<>();
}
