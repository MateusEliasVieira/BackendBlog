package com.seminfo.domain.service;

import com.seminfo.domain.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    public void save(Post post);

    public List<Post> fetchAll();

    public Post findPostById(Long idPost);

    public Page<Post> fetchAllWithPagination(int numberPage);
}
