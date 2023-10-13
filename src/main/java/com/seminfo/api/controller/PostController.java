package com.seminfo.api.controller;

import com.seminfo.api.model.Message;
import com.seminfo.api.model.PostInput;
import com.seminfo.api.model.PostOutput;
import com.seminfo.domain.model.Post;
import com.seminfo.domain.service.PostService;
import com.seminfo.domain.service.impl.PostServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService service;
    private ModelMapper map = new ModelMapper();

    @GetMapping("/all")
    public ResponseEntity<List<PostOutput>> getPosts(){
        System.out.println("Entrou aqui");
        List<PostOutput> list = new ArrayList<PostOutput>();
        service.fetchAll().forEach((post)->{
            list.add(map.map(post,PostOutput.class));
        });
        return new ResponseEntity<List<PostOutput>>(list, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Message> newPost(@RequestBody PostInput postInput){
        Message message = new Message();
        HttpStatus status = null;
        if(service.save(map.map(postInput, Post.class)) != null){
            message.setMessage("Post completed successfully!");
            status = HttpStatus.CREATED;
        }else{
            message.setMessage("Error while posting!");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Message>(message,status);
    }

    @GetMapping("/read-post/{idPost}")
    @ResponseBody
    public ResponseEntity<PostOutput> readPost(@PathVariable Long idPost){
        HttpStatus status = null;
        Post post = service.findPostById(idPost);
        PostOutput postOutput = null;
        if(post != null){
            status = HttpStatus.OK;
            postOutput = map.map(post,PostOutput.class);
        }else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<PostOutput>(postOutput,status);
    }

}
