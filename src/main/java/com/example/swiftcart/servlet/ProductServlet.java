package com.example.swiftcart.servlet;

import com.example.swiftcart.dao.ProductDAO;
import com.example.swiftcart.dao.CategoryDAO;
import com.example.swiftcart.dao.impl.ProductDAOImpl;
import com.example.swiftcart.dao.impl.CategoryDAOImpl;
import com.example.swiftcart.model.Product;
import com.example.swiftcart.model.Category;
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

@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/product"})
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/products".equals(path)) {
            showProductList(req, resp);
        } else if ("/product".equals(path)) {
            showProductDetail(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showProductList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = RequestParamUtil.getPage(req);
        int size = RequestParamUtil.getSize(req);
        if (size <= 0) size = AppConstants.DEFAULT_PAGE_SIZE;

        int categoryId = RequestParamUtil.getInt(req, "category", 0);
        String search = RequestParamUtil.getString(req, "q", null);

        int offset = (page - 1) * size;

        try {
            List<Product> products;
            int total;

            if (search != null && !search.isEmpty()) {
                products = productDAO.searchByName(search, offset, size);
                total = productDAO.countAll();
            } else if (categoryId > 0) {
                products = productDAO.findByCategory(categoryId, offset, size);
                total = productDAO.countByCategory(categoryId);
            } else {
                products = productDAO.findAll(offset, size);
                total = productDAO.countAll();
            }

            int totalPages = (int) Math.ceil((double) total / size);
            List<Category> categories = categoryDAO.findAll();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalPages", totalPages);

            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/product-list.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load products");
        }
    }

    private void showProductDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = RequestParamUtil.getInt(req, "id", -1);
        if (id < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            return;
        }

        try {
            Product p = productDAO.findById(id);
            if (p == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            req.setAttribute("product", p);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load product");
        }
    }
}
