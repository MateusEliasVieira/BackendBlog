package com.seminfo.api.controller;

import com.seminfo.api.model.EmailInput;
import com.seminfo.api.model.EmailOutput;
import com.seminfo.api.model.Message;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/email")
public class EmailController
{

    @Autowired
    private UserService service;

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<Message> confirmMyCountByReceiveEmail(@PathVariable("token") String token)
    {
        Message message = new Message();
        // Decodificando a string Base64
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String tokenDecodedString = new String(decodedBytes);
        User userConfirmed = service.saveUserAfterConfirmedAccountByEmail(tokenDecodedString);
        if(userConfirmed != null)
        {
            // saved
            message.setMessage("Account confirmed successfully!");
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        }
        else
        {
            message.setMessage("Error when confirming account!");
            return new ResponseEntity<Message>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
