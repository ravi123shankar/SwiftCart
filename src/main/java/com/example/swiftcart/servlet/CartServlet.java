package com.example.swiftcart.servlet;

import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import com.example.swiftcart.model.User;
import com.example.swiftcart.service.CartService;
import com.example.swiftcart.service.impl.CartServiceImpl;
import com.example.swiftcart.util.RequestParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/cart".equals(path)) {
            showCart(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            Cart cart = cartService.getCartByUserId(user.getId());
            if ("/cart/add".equals(path)) {
                int productId = RequestParamUtil.getInt(req, "productId", -1);
                int qty = RequestParamUtil.getInt(req, "quantity", 1);
                cartService.addItem(cart.getId(), productId, qty);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            } else if ("/cart/update".equals(path)) {
                int itemId = RequestParamUtil.getInt(req, "itemId", -1);
                int qty = RequestParamUtil.getInt(req, "quantity", 1);
                CartItem item = cartService.getCartItems(cart.getId()).stream()
                        .filter(i -> i.getId() == itemId).findFirst().orElse(null);
                if (item != null) {
                    item.setQuantity(qty);
                    cartService.updateItem(item);
                }
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            } else if ("/cart/remove".equals(path)) {
                int itemId = RequestParamUtil.getInt(req, "itemId", -1);
                cartService.removeItem(itemId);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            } else if ("/cart/clear".equals(path)) {
                cartService.clearCart(cart.getId());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cart operation failed");
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/cart.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load cart");
        }
    }
}
