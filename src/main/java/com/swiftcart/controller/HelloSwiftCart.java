package com.swiftcart.controller;

import com.swiftcart.dao.ProductDAO;
import com.swiftcart.dao.ProductDAOInterface;
import com.swiftcart.exception.DataAccessException;
import com.swiftcart.model.Product;
import com.swiftcart.util.ProductLoaderTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/hello")
public class HelloSwiftCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ProductDAOInterface dao = new ProductDAO();
        ProductLoaderTask task = new ProductLoaderTask(dao);

        Thread dbThread = new Thread(task);
        dbThread.start(); // start background DB work

        try {
            dbThread.join(); // synchronization point
            List<Product> products = task.getProducts();
            req.setAttribute("products", products);

        } catch (InterruptedException e) {
            throw new ServletException("Thread interrupted", e);
        } catch (DataAccessException e) {
            req.setAttribute("error", "Unable to load products at the moment.");
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
