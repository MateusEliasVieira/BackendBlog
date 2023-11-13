package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.api.dto.others.PaginationPost;
import com.seminfo.api.dto.PostInputDTO;
import com.seminfo.api.dto.PostOutputDTO;
import com.seminfo.api.mapper.PostMapper;
import com.seminfo.domain.model.Post;
import com.seminfo.domain.service.PostService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController
{

    @Autowired
    private PostService service;

    @GetMapping("/test-controller")
    public ResponseEntity<Boolean> getTest(){
        return new ResponseEntity<Boolean>(true,HttpStatus.ACCEPTED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PostOutputDTO>> getPosts()
    {
        System.out.println("Entrou aqui");
        List<PostOutputDTO> list = PostMapper.mapperListPostToListPostOutputDTO(service.fetchAll());
        return new ResponseEntity<List<PostOutputDTO>>(list, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<PaginationPost> getPosts(@PathVariable("page") int numberPage)
    {
        System.out.println("entrou para buscar post");
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
    public ResponseEntity<Message> newPost(@RequestBody @Valid PostInputDTO postInputDTO)
    {
        Message message = new Message();
        HttpStatus status = null;

        if(service.save( PostMapper.mapperPostInputDTOToPost(postInputDTO) ) != null)
        {
            message.setMessage("Post completed successfully!");
            status = HttpStatus.CREATED;
        }
        else
        {
            message.setMessage("Error while posting!");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Message>(message,status);
    }

    @GetMapping("/read-post/{idPost}")
    @ResponseBody
    public ResponseEntity<PostOutputDTO> readPost(@PathVariable Long idPost)
    {
        HttpStatus status = null;
        Post post = service.findPostById(idPost);
        PostOutputDTO postOutputDTO = null;
        if(post != null)
        {
            status = HttpStatus.OK;
            postOutputDTO = PostMapper.mapperPostToPostOutputDTO(post);
        }
        else
        {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<PostOutputDTO>(postOutputDTO,status);
    }

}
