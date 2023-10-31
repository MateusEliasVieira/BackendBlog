package com.seminfo.domain.service.impl;

import com.seminfo.domain.exception.UserNotFoundException;
import com.seminfo.domain.model.User;
import com.seminfo.domain.repository.UserRepository;
import com.seminfo.domain.service.UserService;
import com.seminfo.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;
    @Override
    public User save(User user)
    {
        if(repo.findUserByEmail(user.getEmail())==null && repo.findUserByUsername(user.getUsername())==null)
        {
            // empty user
            String firstTokenUser = TokenUtil.getToken(user);
            user.setToken(firstTokenUser);
            user.setStatus(false);
            return repo.save(user);
        }
        else
        {
            return null;
        }
    }

    @Override
    public User saveUserAfterConfirmedAccountByEmail(String token)
    {
        User user = repo.findUserByToken(token);
        if(user != null)
        {
            // token exist from email confimation
            user.setStatus(true);
            return repo.save(user);
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<User> fetchAll() {
        return repo.findAll();
    }

    public User login(User user)
    {
        User userLogin = repo.findByUserAndPassword(user.getUsername(),user.getPassword());
        if(userLogin != null)
        {
            userLogin.setToken(TokenUtil.getToken(user));
            return repo.save(userLogin);
        }
        else
        {
            return null;
        }
    }

    @Override
    public User loginWithGoogle(User user)
    {
        if(repo.findAccountGoogleByEmailAndPassword(user.getEmail(), user.getPassword())==null)
        {
            // empty user
            String firstTokenUser = TokenUtil.getToken(user);
            user.setToken(firstTokenUser);
            user.setStatus(true);
            return repo.save(user);
        }
        else
        {
            // exist user. Update Token
            User userLoginGoogle = repo.findAccountGoogleByEmailAndPassword(user.getEmail(), user.getPassword());
            userLoginGoogle.setToken(TokenUtil.getToken(user));
            repo.save(userLoginGoogle);
        }
        return null;
    }

    @Override
    public User findUser(Long idUser)
    {
       Optional<User> userOptional = repo.findById(idUser);
       return userOptional.isPresent() ? userOptional.get() : userOptional.orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public boolean confirmAccount(String tokenUrl)
    {
        Integer qtdRows = repo.updateStatusUserByToken(tokenUrl);
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
