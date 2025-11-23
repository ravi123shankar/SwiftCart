package com.example.swiftcart.dao.impl;

import com.example.swiftcart.dao.CartDAO;
import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import com.example.swiftcart.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    @Override
    public Cart findCartByUserId(int userId) throws Exception {
        String sql = "SELECT id, user_id, created_at FROM carts WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                Cart c = new Cart();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setCreatedAt(rs.getString("created_at"));
                return c;
            }
            return null;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public int createCart(Cart cart) throws Exception {
        String sql = "INSERT INTO carts (user_id, created_at) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet keys = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cart.getUserId());
            pst.setString(2, cart.getCreatedAt());
            int affected = pst.executeUpdate();
            if (affected == 0) throw new Exception("Creating cart failed.");
            keys = pst.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            throw new Exception("Creating cart failed to return id.");
        } finally {
            JDBCUtil.closeQuietly(keys);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public List<CartItem> findItemsByCartId(int cartId) throws Exception {
        String sql = "SELECT id, cart_id, product_id, quantity, unit_price, subtotal FROM cart_items WHERE cart_id = ? ORDER BY id";
        List<CartItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, cartId);
            rs = pst.executeQuery();
            while (rs.next()) {
                CartItem it = mapRow(rs);
                list.add(it);
            }
            return list;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public CartItem findItemById(int itemId) throws Exception {
        String sql = "SELECT id, cart_id, product_id, quantity, unit_price, subtotal FROM cart_items WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, itemId);
            rs = pst.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } finally {
            JDBCUtil.closeQuietly(rs);
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean addItemToCart(CartItem item) throws Exception {
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, item.getCartId());
            pst.setInt(2, item.getProductId());
            pst.setInt(3, item.getQuantity());
            pst.setDouble(4, item.getUnitPrice());
            pst.setDouble(5, item.getSubtotal());
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean updateCartItem(CartItem item) throws Exception {
        String sql = "UPDATE cart_items SET quantity = ?, unit_price = ?, subtotal = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, item.getQuantity());
            pst.setDouble(2, item.getUnitPrice());
            pst.setDouble(3, item.getSubtotal());
            pst.setInt(4, item.getId());
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean removeItem(int itemId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, itemId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean clearCart(int cartId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, cartId);
            int rows = pst.executeUpdate();
            return true;
        } finally {
            JDBCUtil.closeQuietly(pst);
            JDBCUtil.closeQuietly(conn);
        }
    }

    private CartItem mapRow(ResultSet rs) throws Exception {
        CartItem it = new CartItem();
        it.setId(rs.getInt("id"));
        it.setCartId(rs.getInt("cart_id"));
        it.setProductId(rs.getInt("product_id"));
        it.setQuantity(rs.getInt("quantity"));
        it.setUnitPrice(rs.getDouble("unit_price"));
        it.setSubtotal(rs.getDouble("subtotal"));
        return it;
    }
}
