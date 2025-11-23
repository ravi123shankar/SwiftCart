package com.example.swiftcart.dao;

import com.example.swiftcart.model.User;
import java.util.List;

public interface UserDAO {
    User findById(int id) throws Exception;
    User findByEmail(String email) throws Exception;
    List<User> findAll() throws Exception;
    int create(User user) throws Exception;
    boolean update(User user) throws Exception;
    boolean delete(int id) throws Exception;
}
