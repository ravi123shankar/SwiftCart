package com.example.swiftcart.dao.impl;

import com.example.swiftcart.dao.UserDAO;
import com.example.swiftcart.model.User;
import com.example.swiftcart.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findById(int id) throws Exception {
        String sql = "SELECT id, name, email, password, phone, role, created_at FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                User u = mapRow(rs);
                return u;
            }
            return null;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public User findByEmail(String email) throws Exception {
        String sql = "SELECT id, name, email, password, phone, role, created_at FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, email);
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
    public List<User> findAll() throws Exception {
        String sql = "SELECT id, name, email, password, phone, role, created_at FROM users ORDER BY id";
        List<User> list = new ArrayList<>();
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
    public int create(User user) throws Exception {
        String sql = "INSERT INTO users (name, email, password, phone, role, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet keys = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getCreatedAt());
            int affected = pst.executeUpdate();
            if (affected == 0) throw new Exception("Creating user failed, no rows affected.");
            keys = pst.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new Exception("Creating user failed, no ID obtained.");
            }
        } finally {
            JDBCUtil.closeQuietly(keys);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean update(User user) throws Exception {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, role = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, user.getName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getRole());
            pst.setInt(6, user.getId());
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM users WHERE id = ?";
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

    private User mapRow(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setRole(rs.getString("role"));
        u.setCreatedAt(rs.getString("created_at"));
        return u;
    }
}
