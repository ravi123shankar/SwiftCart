package com.swiftcart.dao;

import com.swiftcart.model.Product;
import com.swiftcart.util.DBConnection;
import com.swiftcart.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements ProductDAOInterface {


    public List<Product> getAllProducts() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT id, name, price FROM products";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed to fetch products from database", e);
        }


        return list;
    }
}
