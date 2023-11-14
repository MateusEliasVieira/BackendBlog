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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
@Tag(name = "user")
public class UserController
{

    @Autowired
    private UserService service;

    @Autowired
    private EmailSenderService emailSenderService;

    @Operation(summary = "Realiza o cadastro de um novo usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email de confirmação da conta foi enviado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Já existe um usuário com o devido email/username cadastrado"),
            @ApiResponse(responseCode = "406", description = "A senha está fraca e deve ser corrigida utilizando letras, números e caracteres especiais"),
            @ApiResponse(responseCode = "404", description = "Erro ao enviar email de confirmação"),
    })
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
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
