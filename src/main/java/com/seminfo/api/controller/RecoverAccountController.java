package com.seminfo.api.controller;

import com.seminfo.api.dto.NewPasswordInputDTO;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import com.seminfo.utils.Feedback;
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
                return new ResponseEntity<String>(Feedback.ACCOUNT_RECOVER_SENT, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<String>(Feedback.EMPTY_USER, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (MessagingException e)
        {
            return new ResponseEntity<String>(Feedback.ERROR_ACCOUNT_RECOVER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody NewPasswordInputDTO newPasswordInputDTO){
        User user = userService.updatePassword(newPasswordInputDTO);
        if(user != null){
            return new ResponseEntity<String>(Feedback.OK_PASSWORD_CHANGE, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(Feedback.ERROR_PASSWORD_CHANGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
