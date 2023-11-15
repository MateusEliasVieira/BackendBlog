package com.seminfo.api.controller;

import com.seminfo.api.dto.NewPasswordInputDTO;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recover")
public class RecoverAccountController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;
    @GetMapping("/recover-account/{email}")
    public ResponseEntity<String> recoverAccount(@PathVariable("email") @Email String email)
    {
        try
        {
            if(emailSenderService.recoverAccount(email))
            {
                return new ResponseEntity<String>("Account recovery email sent successfully!", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<String>("There is no registered user with this email!",HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (MessagingException e)
        {
            return new ResponseEntity<String>("Account recovery email could not be sent!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody NewPasswordInputDTO newPasswordInputDTO){
        User user = userService.updatePassword(newPasswordInputDTO);
        if(user != null){
            return new ResponseEntity<String>("Password changed successfully!",HttpStatus.OK);
        }else{
            return new ResponseEntity<String>("Error when changing password!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
