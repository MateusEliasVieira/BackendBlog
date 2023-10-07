package com.seminfo.api.controller;

import com.seminfo.api.model.Message;
import com.seminfo.api.model.UserInput;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    private ModelMapper map = new ModelMapper();

    @PostMapping("/new")
    public ResponseEntity<Message> newUser(@RequestBody UserInput userInput){
        Message message = new Message();
        if(service.save(map.map(userInput, User.class))!=null){
            // saved
            message.setMessage("New user successfully registered!");
            return new ResponseEntity<Message>(message, HttpStatus.CREATED);
        }else{
            message.setMessage("Error registering new user!");
            return new ResponseEntity<Message>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
