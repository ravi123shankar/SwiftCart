package com.swiftcart.util;

import com.swiftcart.dao.ProductDAOInterface;
import com.swiftcart.model.Product;

import java.util.List;

public class ProductLoaderTask implements Runnable {

    private final ProductDAOInterface dao;
    private List<Product> products;

    public ProductLoaderTask(ProductDAOInterface dao) {
        this.dao = dao;
    }

    @Override
    public void run() {
        products = dao.getAllProducts();
    }

    public List<Product> getProducts() {
        return products;
    }
}
