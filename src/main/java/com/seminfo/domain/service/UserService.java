package com.seminfo.domain.service;

import com.seminfo.domain.model.User;

import java.util.List;

public interface UserService {
    public User save(User user);
    public List<User> fetchAll();
    public User login(User user);
}
