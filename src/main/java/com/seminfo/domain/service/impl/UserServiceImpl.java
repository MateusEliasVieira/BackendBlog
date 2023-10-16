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
    public User save(User user) {
        String firstTokenUser = TokenUtil.getToken(user);
        user.setToken(firstTokenUser);
        user.setStatus(false);
        return repo.save(user);
    }

    @Override
    public List<User> fetchAll() {
        return repo.findAll();
    }

    public User login(User user)
    {
        return repo.
                findByUserAndPassword(user.getUsername(),user.getPassword());

    }

    @Override
    public User findUser(Long idUser) {
       Optional<User> userOptional = repo.findById(idUser);
       return userOptional.isPresent() ? userOptional.get() : userOptional.orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public boolean confirmAccount(String tokenUrl) {
        Integer qtdRows = repo.updateStatusUserByToken(tokenUrl);
        if(qtdRows > 0){
            return true;
        }else{
            return false;
        }
    }
}
