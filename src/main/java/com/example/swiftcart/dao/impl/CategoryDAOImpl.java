package com.example.swiftcart.dao.impl;

import com.example.swiftcart.dao.CategoryDAO;
import com.example.swiftcart.model.Category;
import com.example.swiftcart.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category findById(int id) throws Exception {
        String sql = "SELECT id, name, description FROM categories WHERE id = ?";
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
    public List<Category> findAll() throws Exception {
        String sql = "SELECT id, name, description FROM categories ORDER BY id";
        List<Category> list = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(st);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public int create(Category category) throws Exception {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet keys = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            int affected = pst.executeUpdate();
            if (affected == 0) throw new Exception("Creating category failed, no rows affected.");
            keys = pst.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            throw new Exception("Creating category failed, no ID obtained.");
        } finally {
            JDBCUtil.closeQuietly(keys);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean update(Category category) throws Exception {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            pst.setInt(3, category.getId());
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM categories WHERE id = ?";
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

    private Category mapRow(ResultSet rs) throws Exception {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        return c;
    }
}
