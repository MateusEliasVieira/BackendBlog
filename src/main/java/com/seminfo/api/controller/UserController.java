package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.api.dto.UserInputDTO;
import com.seminfo.api.dto.UserOutputDTO;
import com.seminfo.api.mapper.UserMapper;
import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import com.seminfo.utils.StrongPassword;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController
{

    @Autowired
    private UserService service;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/new")
    public ResponseEntity<Message> newUser(@RequestBody @Valid UserInputDTO userInputDto)
    {
        Message message = new Message();
        HttpStatus httpStatus = null;
        try
        {
            if(StrongPassword.isStrong(userInputDto.getPassword())){
                // password is strong
                User userSaved = service.save( UserMapper.mapperUserInputDTOToUser(userInputDto) );
                if(userSaved != null)
                {
                    emailSenderService.sendEmail(userSaved.getEmail(),userSaved.getToken());
                    message.setMessage("Confirmation email successfully sent to "+userSaved.getEmail());
                    httpStatus = HttpStatus.OK;
                }
                else
                {
                    // not saved
                    message.setMessage("Ops! There is already a user with this email/username registered!");
                    httpStatus = HttpStatus.CONFLICT;
                }
            }
            else
            {
                // password not strong
                message.setMessage("Ops! Your password is not strong! Use letters, numbers and special characters!");
                httpStatus = HttpStatus.NOT_ACCEPTABLE;
            }


        }
        catch (MessagingException e)
        {
            httpStatus = HttpStatus.NOT_FOUND;
            message.setMessage("Error sending confirmation email to "+ userInputDto.getEmail());
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Message>(message,httpStatus);
    }

    @GetMapping ("/find/{idUser}")
    public ResponseEntity<UserOutputDTO> findUser(@PathVariable Long idUser)
    {
        UserOutputDTO userOutputDTO = null;
        HttpStatus status = null;
        try
        {
            userOutputDTO = UserMapper.mapperUserToUserOutputDTO(service.findUser(idUser));
            status = HttpStatus.OK;
        }
        catch (UserNotFoundException error_user)
        {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Error: "+error_user.getMessage());
        }
        catch(Exception error_ex)
        {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Error: "+error_ex.getMessage());
        }
        return new ResponseEntity<UserOutputDTO>(userOutputDTO,status);
    }
}
