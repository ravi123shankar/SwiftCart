package com.example.swiftcart.servlet;

import com.example.swiftcart.dao.ProductDAO;
import com.example.swiftcart.dao.impl.ProductDAOImpl;
import com.example.swiftcart.model.Product;
import com.example.swiftcart.util.RequestParamUtil;
import com.example.swiftcart.config.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = RequestParamUtil.getPage(req);
        int size = RequestParamUtil.getSize(req);
        if (size <= 0) size = AppConstants.DEFAULT_PAGE_SIZE;
        int offset = (page - 1) * size;
        try {
            List<Product> products = productDAO.findAll(offset, size);
            int total = productDAO.countAll();
            int totalPages = (int) Math.ceil((double) total / size);
            req.setAttribute("products", products);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalPages", totalPages);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/home.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load products");
        }
    }
}
