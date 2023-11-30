package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.api.dto.others.PaginationPost;
import com.seminfo.api.dto.post.PostInputDTO;
import com.seminfo.api.dto.post.PostOutputDTO;
import com.seminfo.api.mapper.PostMapper;
import com.seminfo.domain.model.Post;
import com.seminfo.domain.service.PostService;
import com.seminfo.utils.Feedback;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping("/test-controller")
    public ResponseEntity<Boolean> getTest() {
        return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostOutputDTO>> getPosts() {
        List<PostOutputDTO> list = PostMapper.mapperListPostToListPostOutputDTO(service.fetchAll());
        return new ResponseEntity<List<PostOutputDTO>>(list, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<PaginationPost> getPosts(@PathVariable("page") int numberPage) {
        Page<Post> pages = service.fetchAllWithPagination(numberPage);
        pages.toList().forEach(post -> System.out.println(post.getUser().getName()));
        List<PostOutputDTO> list = PostMapper.mapperListPostToListPostOutputDTO(pages.toList());
        PaginationPost paginationPost = new PaginationPost();
        paginationPost.setListPostsOutput(list);
        paginationPost.setQtdPosts(pages.getTotalElements());
        paginationPost.setQtdPages(pages.getTotalPages());
        return new ResponseEntity<PaginationPost>(paginationPost, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Message> newPost(@RequestBody @Valid PostInputDTO postInputDTO) {
        service.save(PostMapper.mapperPostInputDTOToPost(postInputDTO));
        return new ResponseEntity<Message>(new Message(Feedback.POST_COMPLETED), HttpStatus.CREATED);
    }

    @GetMapping("/read-post/{idPost}")
    @ResponseBody
    public ResponseEntity<PostOutputDTO> readPost(@PathVariable Long idPost) {
      return ResponseEntity.ok(PostMapper.mapperPostToPostOutputDTO(service.findPostById(idPost)));
    }

}
