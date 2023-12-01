package com.seminfo.domain.service.impl;


import com.seminfo.api.dto.password.NewPasswordInputDTO;
import com.seminfo.domain.domainException.BusinessRulesException;
import com.seminfo.domain.enums.Roles;
import com.seminfo.domain.model.User;
import com.seminfo.domain.repository.UserRepository;
import com.seminfo.domain.service.UserService;
import com.seminfo.security.jwt.JwtToken;
import com.seminfo.utils.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final int MINUTES_TO_RETRY = 1;

    @Transactional(readOnly = false)
    @Override
    public User save(User user) {
        if (repository.findUserByEmail(user.getEmail()) == null && repository.findUserByUsername(user.getUsername()) == null) {
            // empty user
            String firstTokenUser = JwtToken.generateTokenJWT(user);
            user.setToken(firstTokenUser);
            user.setStatus(false);
            user.setRole(Roles.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userSaved = repository.save(user);
            if(userSaved.getIdUser() == null){
                throw new BusinessRulesException(Feedback.ERROR_CREATE_USER + user.getName());
            }else{
                return userSaved;
            }
        } else {
            throw new BusinessRulesException(Feedback.USER_EXIST);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public User saveUserAfterConfirmedAccountByEmail(String token) {
        User user = repository.findUserByToken(token).orElseThrow(() -> new BusinessRulesException(Feedback.ERROR_CONFIRMATION_ACCOUNT));
        // token exist from email confirmation
        user.setStatus(true);
        return repository.save(user);
    }

    @Transactional(readOnly = false)
    @Override
    public User login(User user) {
        user.setToken(JwtToken.generateTokenJWT(user));
        user.setAttempts(0);
        user.setReleaseLogin(null);
        return repository.save(user);
    }

    @Transactional(readOnly = false)
    @Override
    public User loginWithGoogle(User user) {
        User userLoginWithGoogle = repository.findAccountGoogleByEmail(user.getEmail());
        if (userLoginWithGoogle == null) {
            // empty user
            String firstTokenUser = JwtToken.generateTokenJWT(user);
            user.setToken(firstTokenUser);
            user.setStatus(true);
            user.setRole(Roles.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        } else if (passwordEncoder.matches(user.getPassword(), userLoginWithGoogle.getPassword())) {
            // exist user. Update Token
            userLoginWithGoogle.setToken(JwtToken.generateTokenJWT(userLoginWithGoogle));
            return repository.save(userLoginWithGoogle);
        }
        throw new BusinessRulesException(Feedback.INVALID_LOGIN_BY_GOOGLE);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUser(Long idUser) {
        Optional<User> userOptional = repository.findById(idUser);
        return userOptional.orElseThrow(() -> new BusinessRulesException(Feedback.NOT_EXIST_USER_ID + idUser));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean findUser(String username) {
        Optional<User> userOptional = Optional.ofNullable(repository.findUserByUsername(username));
        return userOptional.isPresent();
    }

    @Transactional(readOnly = false)
    @Override
    public int updateAttempts(String username) {
        int attempts = repository.attemptsUser(username) + 1;
        repository.updateAttemptsUser(attempts, username);
        return repository.attemptsUser(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int attemptsUser(String username) {
        return repository.attemptsUser(username);
    }

    @Transactional(readOnly = false)
    @Override
    public Date releaseLogin(String username) {
        // get current date and time
        LocalDateTime now = LocalDateTime.now();
        // Add minutes
        LocalDateTime minutes = now.plusMinutes(MINUTES_TO_RETRY);
        // release date
        Date releaseDate = Date.from(minutes.toInstant(ZoneOffset.of("-03:00")));
        repository.updateReleaseDate(releaseDate, username);
        return releaseDate;
    }

    @Transactional(readOnly = true)
    @Override
    public Date getDateReleaseLogin(String username) {
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
    public User updatePassword(NewPasswordInputDTO newPasswordInputDTO) {
        User user = repository.findUserByToken(newPasswordInputDTO.getToken()).orElseThrow(() -> new BusinessRulesException(Feedback.ERROR_PASSWORD_CHANGE));
        user.setPassword(passwordEncoder.encode(newPasswordInputDTO.getNewpassword()));
        return repository.save(user);
    }

    @Transactional(readOnly = false)
    @Override
    public User findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }
}
