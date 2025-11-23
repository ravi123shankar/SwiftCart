package com.example.swiftcart.dao;

import com.example.swiftcart.model.Category;
import java.util.List;

public interface CategoryDAO {
    Category findById(int id) throws Exception;
    List<Category> findAll() throws Exception;
    int create(Category category) throws Exception;
    boolean update(Category category) throws Exception;
    boolean delete(int id) throws Exception;
}
