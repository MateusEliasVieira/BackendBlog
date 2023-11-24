package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.api.dto.UserInputDTO;
import com.seminfo.api.dto.UserOutputDTO;
import com.seminfo.api.mapper.UserMapper;
import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import com.seminfo.utils.Feedback;
import com.seminfo.utils.StrongPassword;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
public class UserController
{

    @Autowired
    private UserService service;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> newUser(@RequestBody @Valid UserInputDTO userInputDto, Errors errors)
    {
        Message message = new Message();
        HttpStatus httpStatus = null;
        try
        {
            if(errors.hasErrors()){
                System.out.println(errors.getAllErrors());
            }

            if(StrongPassword.isStrong(userInputDto.getPassword())){
                // password is strong
                User userSaved = service.save( UserMapper.mapperUserInputDTOToUser(userInputDto) );
                if(userSaved != null)
                {
                    emailSenderService.sendEmail(userSaved.getEmail(),userSaved.getToken());
                    message.setMessage(Feedback.SEND_CONF_EMAIL + userSaved.getEmail());
                    httpStatus = HttpStatus.OK;
                }
                else
                {
                    // not saved
                    message.setMessage(Feedback.USER_EXIST);
                    httpStatus = HttpStatus.CONFLICT;
                }
            }
            else
            {
                // password not strong
                message.setMessage(Feedback.WEAK_PASSWORD);
                httpStatus = HttpStatus.NOT_ACCEPTABLE;
            }


        }
        catch (MessagingException e)
        {
            httpStatus = HttpStatus.NOT_FOUND;
            message.setMessage(Feedback.ERROR_SEND_CONF_EMAIL + userInputDto.getEmail());
            //throw new RuntimeException(e);
        }
        catch(Exception e){
            System.out.println("erro = "+e.getMessage());
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
