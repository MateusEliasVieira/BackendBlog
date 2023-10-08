package com.seminfo.api.controller;

import com.seminfo.api.model.Message;
import com.seminfo.api.model.PostInput;
import com.seminfo.api.model.PostOutput;
import com.seminfo.domain.model.Post;
import com.seminfo.domain.service.PostService;
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
        if(service.save(map.map(postInput, Post.class)) != null){
            message.setMessage("Post completed successfully!");
        }else{
            message.setMessage("Error while posting!");
        }
        return new ResponseEntity<Message>(message,HttpStatus.CREATED);
    }

}
