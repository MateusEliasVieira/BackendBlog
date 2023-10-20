package com.seminfo.domain.service.impl;

import com.seminfo.domain.model.Post;
import com.seminfo.domain.repository.PostRepository;
import com.seminfo.domain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repo;

    private final int PAGE_SIZE = 6;
    @Override
    public Post save(Post post) {
        return repo.save(post);
    }

    @Override
    public List<Post> fetchAll() {
        //pageNumber = numero da pagina
        //pageSize = quantidade de elementos por pagina
        return repo.findAllByOrderByDatePublishDesc();
    }

    @Override
    public Page<Post> fetchAllWithPagination(int numberPage) {
        //Sort sort = Sort.by("datePublish").descending(); // ordenation by recent date publish
        //Pageable pageable = PageRequest.of(numberPage,PAGE_SIZE,sort);
        Pageable pageable = PageRequest.of(numberPage,PAGE_SIZE);
        return repo.findAll(pageable);
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
