package com.seminfo.api.controller;

import com.seminfo.api.model.Message;
import com.seminfo.api.model.UserInput;
import com.seminfo.api.model.UserOutput;
import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    private ModelMapper map = new ModelMapper();

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/new")
    public ResponseEntity<Message> newUser(@RequestBody UserInput userInput){
        Message message = new Message();
        HttpStatus httpStatus = null;
        try {
            String token = service.save(map.map(userInput, User.class)).getToken();
            emailSenderService.sendEmail(userInput.getEmail(),token);
            message.setMessage("Confirmation email successfully sent to "+userInput.getEmail());
            httpStatus = HttpStatus.OK;
        } catch (MessagingException e) {
            httpStatus = HttpStatus.NOT_FOUND;
            message.setMessage("Error sending confirmation email to "+userInput.getEmail());
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Message>(message,httpStatus);
    }
    public ResponseEntity<Message> confirmMyCountByReceiveEmail(@RequestBody UserInput userInput){
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


    @GetMapping ("/find/{idUser}")
    public ResponseEntity<UserOutput> findUser(@PathVariable Long idUser){
        UserOutput userOutput = null;
        HttpStatus status = null;
        try{
            userOutput = map.map(service.findUser(idUser),UserOutput.class);
            status = HttpStatus.OK;
        }catch (UserNotFoundException error_user){
            userOutput = null;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Error: "+error_user.getMessage());
        }catch(Exception error_ex){
            userOutput = null;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Error: "+error_ex.getMessage());
        }
        return new ResponseEntity<UserOutput>(userOutput,status);
    }
}
