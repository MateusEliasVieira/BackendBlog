package com.seminfo.domain.service.impl;

import com.seminfo.api.dto.LoginInputDTO;
import com.seminfo.api.dto.LoginOutputDTO;
import com.seminfo.api.dto.NewPasswordInputDTO;
import com.seminfo.api.dto.others.Message;
import com.seminfo.api.mapper.LoginMapper;
import com.seminfo.domain.enums.Permissions;
import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.repository.UserRepository;
import com.seminfo.domain.service.UserService;
import com.seminfo.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository repository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final int MINUTES_TO_RETRY = 1;

    @Transactional(readOnly = false)
    @Override
    public User save(User user)
    {
        if(repository.findUserByEmail(user.getEmail())==null && repository.findUserByUsername(user.getUsername())==null)
        {
            // empty user
            String firstTokenUser = TokenUtil.getToken(user);
            user.setToken(firstTokenUser);
            user.setStatus(false);
            user.setPermission(Permissions.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        }
        else
        {
            return null;
        }
    }

    @Transactional(readOnly = false)
    @Override
    public User saveUserAfterConfirmedAccountByEmail(String token)
    {
        User user = repository.findUserByToken(token);
        if(user != null)
        {
            // token exist from email confirmation
            user.setStatus(true);
            return repository.save(user);
        }
        else
        {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> fetchAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = false)
    @Override
    public User login(User user)
    {
        User userLogin = repository.findUserByUsername(user.getUsername());
        if(userLogin != null && passwordEncoder.matches(user.getPassword(), userLogin.getPassword()))
        {
            userLogin.setToken(TokenUtil.getToken(userLogin));
            userLogin.setAttempts(0);
            userLogin.setReleaseLogin(null);
            return repository.save(userLogin);
        }
        else
        {
            return null;
        }
    }

    @Transactional(readOnly = false)
    @Override
    public User loginWithGoogle(User user)
    {
        User userLoginWithGoogle = repository.findAccountGoogleByEmail(user.getEmail());
        if(userLoginWithGoogle==null)
        {
            // empty user
            String firstTokenUser = TokenUtil.getToken(user);
            user.setToken(firstTokenUser);
            user.setStatus(true);
            user.setPermission(Permissions.USER);
            return repository.save(user);
        }
        else if(passwordEncoder.matches(user.getPassword(),userLoginWithGoogle.getPassword()))
        {
            // exist user. Update Token
            userLoginWithGoogle.setToken(TokenUtil.getToken(userLoginWithGoogle));
            return repository.save(userLoginWithGoogle);
        }

        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public User findUser(Long idUser)
    {
       Optional<User> userOptional = repository.findById(idUser);
       return userOptional.isPresent() ? userOptional.get() : userOptional.orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean findUser(String username)
    {
        Optional<User> userOptional = Optional.ofNullable(repository.findUserByUsername(username));
        return userOptional.isPresent();
    }

    @Transactional(readOnly = false)
    @Override
    public int updateAttempts(String username) {
        int attempts = repository.attemptsUser(username) + 1;
        repository.updateAttemptsUser(attempts,username);
        return repository.attemptsUser(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int attemptsUser(String username) {
        return repository.attemptsUser(username);
    }

    @Transactional(readOnly = false)
    @Override
    public Date releaseLogin(String username){
        // get current date and time
        LocalDateTime now = LocalDateTime.now();
        // Add minutes
        LocalDateTime minutes = now.plusMinutes(MINUTES_TO_RETRY);
        // release date
        Date releaseDate = Date.from(minutes.toInstant(ZoneOffset.of("-03:00")));
        repository.updateReleaseDate(releaseDate,username);
        return releaseDate;
    }

    @Transactional(readOnly = true)
    @Override
    public Date getDateReleaseLogin(String username){
        return repository.getDateReleaseLogin(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean verifyReleaseDateLogin(String username) {
        return repository.getDateReleaseLogin(username) != null;
    }

    @Transactional(readOnly = false)
    @Override
    public void resetAttemptsAndReleaseLogin(String username) {
        repository.resetAttemptsAndReleaseLogin(username);
    }

    @Transactional(readOnly = false)
    @Override
    public boolean confirmAccount(String tokenUrl)
    {
        Integer qtdRows = repository.updateStatusUserByToken(tokenUrl);
        if(qtdRows > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Transactional(readOnly = false)
    @Override
    public User updatePassword(NewPasswordInputDTO newPasswordInputDTO) {
        User user = repository.findUserByToken(newPasswordInputDTO.getToken());
        user.setPassword(passwordEncoder.encode(newPasswordInputDTO.getNewpassword()));
        return repository.save(user);
    }


}
