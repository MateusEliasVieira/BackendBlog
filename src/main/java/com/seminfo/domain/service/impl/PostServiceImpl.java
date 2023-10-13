package com.seminfo.domain.service.impl;

import com.seminfo.domain.model.Post;
import com.seminfo.domain.repository.PostRepository;
import com.seminfo.domain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Post findPostById(Long idPost){
        Optional<Post> optional = repo.findById(idPost);
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }
}
