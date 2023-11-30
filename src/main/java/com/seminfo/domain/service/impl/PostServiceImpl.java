package com.seminfo.domain.service.impl;

import com.seminfo.domain.domainException.BusinessRulesException;
import com.seminfo.domain.model.Post;
import com.seminfo.domain.repository.PostRepository;
import com.seminfo.domain.service.PostService;
import com.seminfo.utils.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repository;
    private final int PAGE_SIZE = 6;

    @Transactional(readOnly = false)
    @Override
    public void save(Post post) {
        Post postSaved = repository.save(post);
        if (postSaved.getIdPost() == null) {
            throw new BusinessRulesException(Feedback.ERROR_CREATE_POST);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> fetchAll() {
        //pageNumber = numero da pagina
        //pageSize = quantidade de elementos por pagina
        return repository.findAllByOrderByDatePublishDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> fetchAllWithPagination(int numberPage) {
        Sort sort = Sort.by("datePublish").descending(); // ordenation by recent date publish
        Pageable pageable = PageRequest.of(numberPage, PAGE_SIZE, sort);
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Post findPostById(Long idPost) {
        return repository.findById(idPost).orElseThrow(() -> new BusinessRulesException(Feedback.NOT_EXIST_POST_ID + idPost));
    }
}
