package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.api.dto.user.UserInputDTO;
import com.seminfo.api.mapper.UserMapper;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import com.seminfo.utils.Feedback;
import com.seminfo.utils.StrongPassword;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> newUser(@RequestBody @Valid UserInputDTO userInputDto) {
        StrongPassword.isStrong(userInputDto.getPassword());
        User user = service.save(UserMapper.mapperUserInputDTOToUser(userInputDto));
        emailSenderService.sendEmail(user.getEmail(), user.getToken());
        return new ResponseEntity<Message>(new Message(Feedback.SEND_CONF_EMAIL + user.getEmail()), HttpStatus.CREATED);
    }

    @GetMapping("/find/{idUser}")
    public ResponseEntity<?> findUser(@PathVariable Long idUser) {
        return ResponseEntity.ok(UserMapper.mapperUserToUserOutputDTO(service.findUser(idUser)));
    }
}
