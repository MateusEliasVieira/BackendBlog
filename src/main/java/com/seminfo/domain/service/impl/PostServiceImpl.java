package com.seminfo.domain.service.impl;

import com.seminfo.domain.model.Post;
import com.seminfo.domain.repository.PostRepository;
import com.seminfo.domain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repo;
    @Override
    public Post save(Post post) {
        return repo.save(post);
    }

    @Override
    public List<Post> fetchAll() {
        return repo.findAll();
    }
}
