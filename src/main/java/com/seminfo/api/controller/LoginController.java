package com.seminfo.api.controller;

import com.seminfo.api.dto.LoginInputDTO;
import com.seminfo.api.dto.LoginInputGoogleDTO;
import com.seminfo.api.dto.LoginOutputDTO;
import com.seminfo.api.mapper.LoginMapper;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.UserService;
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

@RestController
@RequestMapping("/login")
public class LoginController
{

    @Autowired
    private UserService service;

    @PostMapping("/enter")
    public ResponseEntity<LoginOutputDTO> enter(@RequestBody @Valid LoginInputDTO loginInputDTO, HttpServletRequest request)
    {
        // cria um log da requisição de login
        Log.createSimpleLog(loginInputDTO,request);

        User user = LoginMapper.mapperLoginInputDTOToUser(loginInputDTO);
        User loggedInUser = service.login(user);

        if(loggedInUser != null && loggedInUser.isStatus())
        {
            // Exist user and password // status is true

            LoginOutputDTO loginOutputDTO = new LoginOutputDTO(loggedInUser.getIdUser(),loggedInUser.getToken(),loggedInUser.getPermission());

            return new ResponseEntity<LoginOutputDTO>(loginOutputDTO,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<LoginOutputDTO>((LoginOutputDTO) null,HttpStatus.NO_CONTENT);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginOutputDTO> enterWithGoogle(@RequestBody @Valid LoginInputGoogleDTO loginInputGoogleDTO, HttpServletRequest request)
    {

        // cria um log da requisição de login
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
