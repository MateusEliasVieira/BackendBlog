package com.seminfo.api.controller;

import com.seminfo.api.dto.LoginInputDTO;
import com.seminfo.api.dto.LoginInputGoogleDTO;
import com.seminfo.api.dto.LoginOutputDTO;
import com.seminfo.api.dto.others.Message;
import com.seminfo.api.mapper.LoginMapper;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.UserService;
import com.seminfo.utils.Feedback;
import com.seminfo.utils.FormatDate;
import com.seminfo.utils.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/login")
public class LoginController
{

    @Autowired
    private UserService service;
    private final int MAX_ATTEMPTS = 3;

    @PostMapping("/enter")
    public ResponseEntity<?> enter(@RequestBody @Valid LoginInputDTO loginInputDTO, HttpServletRequest request)
    {
        Message message = new Message();

        // creates a log of the login request
        Log.createSimpleLog(loginInputDTO, request);

        if(service.findUser(loginInputDTO.getUsername())){

            if(service.attemptsUser(loginInputDTO.getUsername()) < MAX_ATTEMPTS)
            {
                return processLogin(loginInputDTO);
            }
            else
            {
                // At this point, we know that the allowed number of attempts has been exceeded
                // check if there is a waiting date for a new login attempt
                if(service.verifyReleaseDateLogin(loginInputDTO.getUsername()))
                {

                    // there is a lockout date for login
                    Date releaseDate = service.getDateReleaseLogin(loginInputDTO.getUsername());

                    // check if it is still valid
                    if(releaseDate.after(new Date(System.currentTimeMillis())))
                    {
                        // if it is not expired yet
                        message.setMessage(Feedback.NEW_ATTEMPT + FormatDate.formatMyDate(releaseDate));
                        return new ResponseEntity<Message>(message, HttpStatus.LOCKED);
                    }
                    else
                    {
                        // time expired
                        service.resetAttemptsAndReleaseLogin(loginInputDTO.getUsername());
                        return processLogin(loginInputDTO);
                    }
                }
                else
                {
                    // If it doesn't exist, add the waiting time for a new login attempt for this user
                    Date releaseDate = service.releaseLogin(loginInputDTO.getUsername());
                    message.setMessage(Feedback.EXHAUSTED_ATTEMPTS + FormatDate.formatMyDate(releaseDate));
                    return new ResponseEntity<Message>(message, HttpStatus.LOCKED);
                }

            }

        }
        message.setMessage(Feedback.INVALID_LOGIN);
        return new ResponseEntity<Message>(message, HttpStatus.NOT_ACCEPTABLE);
    }

    private ResponseEntity<?> processLogin(LoginInputDTO loginInputDTO)
    {
        // username exists and is still within the maximum allowed number of attempts
        User user = LoginMapper.mapperLoginInputDTOToUser(loginInputDTO);
        User loggedInUser = service.login(user);

        // check login
        if(loggedInUser != null && loggedInUser.isStatus())
        {
            // User and password exist, and status is true
            LoginOutputDTO loginOutputDTO = new LoginOutputDTO(loggedInUser.getIdUser(), loggedInUser.getToken(), loggedInUser.getPermission());
            return new ResponseEntity<LoginOutputDTO>(loginOutputDTO, HttpStatus.ACCEPTED);
        }

        // increment attempts
        service.updateAttempts(loginInputDTO.getUsername());
        return new ResponseEntity<Message>(new Message(Feedback.INVALID_LOGIN), HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginOutputDTO> enterWithGoogle(@RequestBody @Valid LoginInputGoogleDTO loginInputGoogleDTO, HttpServletRequest request)
    {

        // creates a log of the login request
        Log.createGoogleLog(loginInputGoogleDTO,request);

        User user = LoginMapper.mapperLoginInputGoogleDTOToUser(loginInputGoogleDTO);
        User loggedInUser = service.loginWithGoogle(user);
        if(loggedInUser != null && loggedInUser.isStatus())
        {
            // Exist email and password // status is true

            LoginOutputDTO loginOutputDTO = new LoginOutputDTO(loggedInUser.getIdUser(),loggedInUser.getToken(),loggedInUser.getPermission());

            return new ResponseEntity<LoginOutputDTO>(loginOutputDTO,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<LoginOutputDTO>((LoginOutputDTO) null,HttpStatus.NO_CONTENT);
    }


}
