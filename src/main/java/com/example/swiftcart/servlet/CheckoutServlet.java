package com.example.swiftcart.servlet;

import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import com.example.swiftcart.model.User;
import com.example.swiftcart.service.CartService;
import com.example.swiftcart.service.impl.CartServiceImpl;
import com.example.swiftcart.util.JDBCUtil;
import com.example.swiftcart.util.RequestParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/checkout/place"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            Cart cart = cartService.getCartByUserId(user.getId());
            List<CartItem> items = cartService.getCartItems(cart.getId());
            double total = items.stream().mapToDouble(CartItem::getSubtotal).sum();
            req.setAttribute("items", items);
            req.setAttribute("total", total);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load checkout");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String street = RequestParamUtil.getString(req, "street");
        String city = RequestParamUtil.getString(req, "city");
        String state = RequestParamUtil.getString(req, "state");
        String postal = RequestParamUtil.getString(req, "postalCode");
        String country = RequestParamUtil.getString(req, "country", "India");
        if (street == null || city == null || postal == null) {
            req.setAttribute("error", "Address fields required");
            doGet(req, resp);
            return;
        }
        try {
            Cart cart = cartService.getCartByUserId(user.getId());
            List<CartItem> items = cartService.getCartItems(cart.getId());
            if (items.isEmpty()) {
                req.setAttribute("error", "Cart is empty");
                doGet(req, resp);
                return;
            }
            double total = items.stream().mapToDouble(CartItem::getSubtotal).sum();
            Connection conn = null;
            PreparedStatement pst = null;
            ResultSet keys = null;
            int addressId = -1;
            int orderId = -1;
            try {
                conn = JDBCUtil.getConnection();
                conn.setAutoCommit(false);
                String insertAddress = "INSERT INTO addresses (user_id, street, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, user.getId());
                pst.setString(2, street);
                pst.setString(3, city);
                pst.setString(4, state);
                pst.setString(5, postal);
                pst.setString(6, country);
                int aRows = pst.executeUpdate();
                if (aRows == 0) throw new Exception("Failed to insert address");
                keys = pst.getGeneratedKeys();
                if (keys.next()) addressId = keys.getInt(1);
                JDBCUtil.closeQuietly(keys);
                JDBCUtil.closeQuietly(pst);
                String insertOrder = "INSERT INTO orders (user_id, order_date, total_amount, status, address_id, payment_id) VALUES (?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, user.getId());
                pst.setString(2, LocalDateTime.now().format(dtf));
                pst.setDouble(3, total);
                pst.setString(4, "PLACED");
                pst.setInt(5, addressId);
                pst.setString(6, null);
                int oRows = pst.executeUpdate();
                if (oRows == 0) throw new Exception("Failed to create order");
                keys = pst.getGeneratedKeys();
                if (keys.next()) orderId = keys.getInt(1);
                JDBCUtil.closeQuietly(keys);
                JDBCUtil.closeQuietly(pst);
                String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(insertItem);
                for (CartItem it : items) {
                    pst.setInt(1, orderId);
                    pst.setInt(2, it.getProductId());
                    pst.setInt(3, it.getQuantity());
                    pst.setDouble(4, it.getUnitPrice());
                    pst.setDouble(5, it.getSubtotal());
                    pst.addBatch();
                }
                pst.executeBatch();
                conn.commit();
            } catch (Exception ex) {
                if (conn != null) try { conn.rollback(); } catch (Exception ignored) {}
                throw ex;
            } finally {
                JDBCUtil.closeQuietly(keys);
                JDBCUtil.closeQuietly(pst);
                if (conn != null) try { conn.setAutoCommit(true); } catch (Exception ignored) {}
                JDBCUtil.closeQuietly(conn);
            }
            cartService.clearCart(cart.getId());
            req.setAttribute("message", "Order placed successfully. Order ID: " + orderId);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Checkout failed");
        }
    }
}
