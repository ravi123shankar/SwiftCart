package com.example.swiftcart.dao;

import com.example.swiftcart.model.Cart;
import com.example.swiftcart.model.CartItem;
import java.util.List;

public interface CartDAO {

    Cart findCartByUserId(int userId) throws Exception;

    int createCart(Cart cart) throws Exception;

    List<CartItem> findItemsByCartId(int cartId) throws Exception;

    CartItem findItemById(int itemId) throws Exception;

    boolean addItemToCart(CartItem item) throws Exception;

    boolean updateCartItem(CartItem item) throws Exception;

    boolean removeItem(int itemId) throws Exception;

    boolean clearCart(int cartId) throws Exception;
}
