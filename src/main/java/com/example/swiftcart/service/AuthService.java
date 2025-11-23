package com.example.swiftcart.service;

import com.example.swiftcart.model.User;

public interface AuthService {

    int register(User user) throws Exception;
    User authenticate(String email, String password) throws Exception;
    User findById(int id) throws Exception;
}
