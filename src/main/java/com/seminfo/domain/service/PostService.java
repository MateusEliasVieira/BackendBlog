package com.seminfo.domain.service;

import com.seminfo.domain.model.Post;

import java.util.List;

public interface PostService {
    public Post save(Post post);
    public List<Post> fetchAll();
}
