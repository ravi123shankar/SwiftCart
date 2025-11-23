package com.example.swiftcart.dao.impl;

import com.example.swiftcart.dao.ProductDAO;
import com.example.swiftcart.model.Product;
import com.example.swiftcart.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product findById(int id) throws Exception {
        String sql = "SELECT id, name, description, price, stock, category_id, image_url, created_at FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<Product> findAll(int offset, int limit) throws Exception {
        String sql = "SELECT id, name, description, price, stock, category_id, image_url, created_at FROM products ORDER BY id LIMIT ? OFFSET ?";
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, limit);
            pst.setInt(2, offset);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<Product> findByCategory(int categoryId, int offset, int limit) throws Exception {
        String sql = "SELECT id, name, description, price, stock, category_id, image_url, created_at FROM products WHERE category_id = ? ORDER BY id LIMIT ? OFFSET ?";
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, categoryId);
            pst.setInt(2, limit);
            pst.setInt(3, offset);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<Product> searchByName(String query, int offset, int limit) throws Exception {
        String sql = "SELECT id, name, description, price, stock, category_id, image_url, created_at FROM products WHERE name LIKE ? ORDER BY id LIMIT ? OFFSET ?";
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + query + "%");
            pst.setInt(2, limit);
            pst.setInt(3, offset);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public int create(Product product) throws Exception {
        String sql = "INSERT INTO products (name, description, price, stock, category_id, image_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet keys = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, product.getName());
            pst.setString(2, product.getDescription());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getStock());
            pst.setInt(5, product.getCategoryId());
            pst.setString(6, product.getImageUrl());
            pst.setString(7, product.getCreatedAt());
            int affected = pst.executeUpdate();
            if (affected == 0) throw new Exception("Creating product failed, no rows affected.");
            keys = pst.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new Exception("Creating product failed, no ID obtained.");
            }
        } finally {
            JDBCUtil.closeQuietly(keys);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean update(Product product) throws Exception {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category_id = ?, image_url = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, product.getName());
            pst.setString(2, product.getDescription());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getStock());
            pst.setInt(5, product.getCategoryId());
            pst.setString(6, product.getImageUrl());
            pst.setInt(7, product.getId());
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public int countAll() throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM products";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) return rs.getInt("cnt");
            return 0;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(st);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public int countByCategory(int categoryId) throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM products WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, categoryId);
            rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("cnt");
            return 0;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    private Product mapRow(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getString("created_at"));
        return p;
    }
}
