package com.example.swiftcart.service;

import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import java.util.List;

public interface CartService {

    Cart getCartByUserId(int userId) throws Exception;

    int createCart(int userId) throws Exception;

    List<CartItem> getCartItems(int cartId) throws Exception;

    boolean addItem(int cartId, int productId, int quantity) throws Exception;

    boolean updateItem(CartItem item) throws Exception;

    boolean removeItem(int itemId) throws Exception;

    boolean clearCart(int cartId) throws Exception;
}
