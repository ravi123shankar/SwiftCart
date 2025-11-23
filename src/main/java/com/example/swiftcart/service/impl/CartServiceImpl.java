package com.example.swiftcart.service.impl;

import com.example.swiftcart.dao.CartDAO;
import com.example.swiftcart.dao.ProductDAO;
import com.example.swiftcart.dao.impl.CartDAOImpl;
import com.example.swiftcart.dao.impl.ProductDAOImpl;
import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import com.example.swiftcart.model.Product;
import com.example.swiftcart.service.CartService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO = new CartDAOImpl();
    private final ProductDAO productDAO = new ProductDAOImpl();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Cart getCartByUserId(int userId) throws Exception {
        Cart c = cartDAO.findCartByUserId(userId);
        if (c == null) {
            int id = createCart(userId);
            c = new Cart();
            c.setId(id);
            c.setUserId(userId);
            c.setCreatedAt(LocalDateTime.now().format(dtf));
        }
        return c;
    }

    @Override
    public int createCart(int userId) throws Exception {
        Cart c = new Cart();
        c.setUserId(userId);
        c.setCreatedAt(LocalDateTime.now().format(dtf));
        return cartDAO.createCart(c);
    }

    @Override
    public List<CartItem> getCartItems(int cartId) throws Exception {
        return cartDAO.findItemsByCartId(cartId);
    }

    @Override
    public boolean addItem(int cartId, int productId, int quantity) throws Exception {
        Product p = productDAO.findById(productId);
        if (p == null) return false;
        double unit = p.getPrice();
        double subtotal = unit * quantity;
        CartItem it = new CartItem();
        it.setCartId(cartId);
        it.setProductId(productId);
        it.setQuantity(quantity);
        it.setUnitPrice(unit);
        it.setSubtotal(subtotal);
        return cartDAO.addItemToCart(it);
    }

    @Override
    public boolean updateItem(CartItem item) throws Exception {
        Product p = productDAO.findById(item.getProductId());
        if (p == null) return false;
        item.setUnitPrice(p.getPrice());
        item.setSubtotal(item.getUnitPrice() * item.getQuantity());
        return cartDAO.updateCartItem(item);
    }

    @Override
    public boolean removeItem(int itemId) throws Exception {
        return cartDAO.removeItem(itemId);
    }

    @Override
    public boolean clearCart(int cartId) throws Exception {
        return cartDAO.clearCart(cartId);
    }
}
