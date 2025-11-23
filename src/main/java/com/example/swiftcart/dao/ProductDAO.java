package com.example.swiftcart.dao;

import com.example.swiftcart.model.Product;
import java.util.List;

public interface ProductDAO {
    Product findById(int id) throws Exception;
    List<Product> findAll(int offset, int limit) throws Exception;
    List<Product> findByCategory(int categoryId, int offset, int limit) throws Exception;
    List<Product> searchByName(String query, int offset, int limit) throws Exception;
    int create(Product product) throws Exception;
    boolean update(Product product) throws Exception;
    boolean delete(int id) throws Exception;
    int countAll() throws Exception;
    int countByCategory(int categoryId) throws Exception;
}
