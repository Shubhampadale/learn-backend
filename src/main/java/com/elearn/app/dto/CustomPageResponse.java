package com.elearn.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageResponse<T> {

    //here since this response we have created is specific to the CategoryDTO.
    //In similar manner if want to perform for the video/course then need to create different classes..
    //So resolve this, we can make use of Generics...which is of Parameterized type
    //Now below at List instead of passing CategoryDto we will pass the T
    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean isLast;

    //private List<CategoryDto> content;

    private List<T> content;
}
