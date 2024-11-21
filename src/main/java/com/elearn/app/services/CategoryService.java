package com.elearn.app.services;

import com.elearn.app.dto.CategoryDto;
import com.elearn.app.dto.CourseDto;
import com.elearn.app.dto.CustomPageResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {

    CategoryDto insert(CategoryDto categoryDto);

    CustomPageResponse<CategoryDto> getAll(int pageNumber, int pageSize,String sortBy);

    CategoryDto get(String categoryId);

    void delete(String categoryId);

    CategoryDto update(CategoryDto categoryDto, String categoryId);

    public void addCourseToCategory(String catId, String course);


    public List<CourseDto> getCoursesOfCat(String categoryId);
}
