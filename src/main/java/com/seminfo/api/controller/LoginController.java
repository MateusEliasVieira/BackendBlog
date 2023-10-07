package com.seminfo.api.controller;

import com.seminfo.api.model.LoginInput;
import com.seminfo.api.model.LoginOutput;
import com.seminfo.domain.model.User;
import com.seminfo.security.TokenUtil;
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
@RequestMapping("/login")
public class LoginController {

    private ModelMapper map = new ModelMapper();
    @Autowired
    private UserService service;
    @PostMapping("/enter")
    public ResponseEntity<LoginOutput> enter(@RequestBody LoginInput loginInput){
        User user = map.map(loginInput, User.class);
        User loggedInUser = service.login(user);
        if(loggedInUser != null) {

            // Exist user and password
            User usuario = new User();
            user.setUsername(loggedInUser.getUsername());

            String token = TokenUtil.getToken(usuario);
            LoginOutput loginOutput = new LoginOutput(loggedInUser.getIdUser(),token);

            return new ResponseEntity<LoginOutput>(loginOutput,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<LoginOutput>((LoginOutput) null,HttpStatus.NO_CONTENT);
    }
}
