package com.seminfo.domain.service;

import com.seminfo.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public Post save(Post post);
    public List<Post> fetchAll();
    public Post findPostById(Long idPost);
}
