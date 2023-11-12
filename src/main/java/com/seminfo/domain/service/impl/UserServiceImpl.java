package com.seminfo.domain.service.impl;

import com.seminfo.domain.enums.Permissions;
import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.repository.UserRepository;
import com.seminfo.domain.service.UserService;
import com.seminfo.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository repository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            System.out.println("OK");
            userLogin.setToken(TokenUtil.getToken(userLogin));
            return repository.save(userLogin);
        }
        else
        {
            System.out.println("Nao deu para encontrar o usuario");
            return null;
        }
    }

    @Transactional(readOnly = false)
    @Override
    public User loginWithGoogle(User user)
    {
        User userLoginWithGoogle = repository.findAccountGoogleByEmail(user.getEmail());
        System.out.println("senha = "+passwordEncoder.encode(user.getPassword()));
        if(userLoginWithGoogle==null)
        {
            System.out.println("nao tem conta");
            // empty user
            String firstTokenUser = TokenUtil.getToken(user);
            user.setToken(firstTokenUser);
            user.setStatus(true);
            user.setPermission(Permissions.USER);
            return repository.save(user);
        }
        else if(passwordEncoder.matches(user.getPassword(),userLoginWithGoogle.getPassword()))
        {
            System.out.println("tem conta");
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
}
