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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService
{

    @Autowired
    private PostRepository repository;
    private final int PAGE_SIZE = 6;
    
    @Transactional(readOnly = false)
    @Override
    public Post save(Post post)
    {
        return repository.save(post);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> fetchAll()
    {
        //pageNumber = numero da pagina
        //pageSize = quantidade de elementos por pagina
        return repository.findAllByOrderByDatePublishDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> fetchAllWithPagination(int numberPage)
    {
        //Sort sort = Sort.by("datePublish").descending(); // ordenation by recent date publish
        //Pageable pageable = PageRequest.of(numberPage,PAGE_SIZE,sort);
        Pageable pageable = PageRequest.of(numberPage,PAGE_SIZE);
        return repository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Post findPostById(Long idPost)
    {
        Optional<Post> optional = repository.findById(idPost);
        if(optional.isPresent())
        {
            return optional.get();
        }
        else
        {
            return null;
        }
    }
}
