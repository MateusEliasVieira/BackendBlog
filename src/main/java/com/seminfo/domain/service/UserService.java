package com.seminfo.domain.service;

import com.seminfo.domain.model.User;

import java.util.List;

public interface UserService {
    public User save(User user);
    public User saveUserAfterConfirmedAccountByEmail(String token);
    public List<User> fetchAll();
    public User login(User user);
    public User loginWithGoogle(User user);
    public User findUser(Long idUser);
    public boolean confirmAccount(String tokenUrl);
}
