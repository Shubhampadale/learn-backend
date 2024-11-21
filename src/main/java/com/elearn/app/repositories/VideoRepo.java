package com.elearn.app.repositories;

import com.elearn.app.entities.Course;
import com.elearn.app.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepo extends JpaRepository<Video,String> {

    List<Video> findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword,String keyword1);
}
