package com.elearn.app.controllers;

import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.CategoryDto;
import com.elearn.app.dto.CourseDto;
import com.elearn.app.dto.CustomMessage;
import com.elearn.app.dto.CustomPageResponse;
import com.elearn.app.services.CategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController{


    private CategoryService categoryService;



    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }






//category: create

    //since we have to return the response containing status as well along with data...
    @PostMapping
    public ResponseEntity<CategoryDto> create(
           @RequestBody @Valid CategoryDto categoryDto
           //BindingResult bindingResult
    ){
        //if(bindingResult.hasErrors()){
          //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Invalid Data);
        //}
         CategoryDto categoryDto1 =    categoryService.insert(categoryDto);
         return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto1);
    }

    //category : get all
    @GetMapping
    public CustomPageResponse<CategoryDto> getAll(
            @RequestParam(value="pageNumber",required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy
    ){

        return categoryService.getAll(pageNumber,pageSize,sortBy);
    }

    //category: get single

    @GetMapping("/{categoryId}")
    public CategoryDto getSingle(
            @PathVariable String categoryId
    ){
        return categoryService.get(categoryId);

    }

    //category: delete

    @DeleteMapping("/{catId}")
    public ResponseEntity<CustomMessage> delete(
           @PathVariable String catId
    ){
        categoryService.delete(catId);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Category Deleted Successfully!!!");
        customMessage.setSuccess(true);
        return ResponseEntity.status(HttpStatus.OK).body(customMessage);
    }

    @PutMapping("/{catId}")
    //category: update
    public CategoryDto updateCategory(
            @PathVariable String catId,
            @RequestBody  CategoryDto categoryDto
    ){

       CategoryDto categoryDtoUpdate =  categoryService.update(categoryDto,catId);
       return categoryDtoUpdate;
    }

    @PostMapping("{categoryId}/courses/{courseId}")
    public ResponseEntity<CustomMessage> addCourseToCategory(
            @PathVariable String categoryId,
            @PathVariable String courseId
    ){

        categoryService.addCourseToCategory(categoryId,courseId);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Category Updated!!!");
        customMessage.setSuccess(true);
        return ResponseEntity.ok(customMessage);
    }

    @GetMapping("/{categoryId}/courses")
    public ResponseEntity<List<CourseDto>> getCoursesOfCategory(
            @PathVariable String categoryId
    ){

        return ResponseEntity.ok(categoryService.getCoursesOfCat(categoryId));

    }

}
