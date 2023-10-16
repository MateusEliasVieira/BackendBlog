package com.seminfo.domain.repository;

import com.seminfo.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

//    @Query("SELECT p FROM Post ORDER BY p.datePublish ASC")
    public List<Post> findAllByOrderByDatePublishDesc();
}
