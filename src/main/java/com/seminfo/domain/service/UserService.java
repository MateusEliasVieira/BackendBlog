package com.seminfo.domain.service;

import com.seminfo.api.dto.LoginInputDTO;
import com.seminfo.api.dto.NewPasswordInputDTO;
import com.seminfo.domain.enums.Roles;
import com.seminfo.domain.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface UserService {
    public User save(User user);
    public User saveUserAfterConfirmedAccountByEmail(String token);
    public List<User> fetchAll();
    public User login(String username);
    public User loginWithGoogle(User user);
    public User findUser(Long idUser);
    public Boolean findUser(String username);
    public User findUserByUsername(String username);
    public int updateAttempts(String username);
    public int attemptsUser(String username);
    public Date releaseLogin(String username);
    public Boolean verifyReleaseDateLogin(String username);
    public Date getDateReleaseLogin(String username);
    public void resetAttemptsAndReleaseLogin(String username);
    public User updatePassword(NewPasswordInputDTO newPasswordInputDTO);
    public Roles findRoleByUsername(String username);
    public boolean confirmAccount(String tokenUrl);
}
