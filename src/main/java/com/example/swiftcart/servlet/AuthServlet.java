package com.example.swiftcart.servlet;

import com.example.swiftcart.dao.UserDAO;
import com.example.swiftcart.dao.impl.UserDAOImpl;
import com.example.swiftcart.model.User;
import com.example.swiftcart.util.PasswordUtil;
import com.example.swiftcart.util.RequestParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/logout".equals(path)) {
            req.getSession().removeAttribute("user");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // show login page
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/login.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = RequestParamUtil.getString(req, "email");
        String password = RequestParamUtil.getString(req, "password");

        if (email == null || password == null) {
            req.setAttribute("error", "Email and password are required.");
            forwardLogin(req, resp);
            return;
        }

        try {
            User u = userDAO.findByEmail(email);
            if (u == null || !PasswordUtil.matches(password, u.getPassword())) {
                req.setAttribute("error", "Invalid credentials.");
                forwardLogin(req, resp);
                return;
            }

            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            req.setAttribute("error", "Login failed: " + e.getMessage());
            forwardLogin(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = RequestParamUtil.getString(req, "name");
        String email = RequestParamUtil.getString(req, "email");
        String password = RequestParamUtil.getString(req, "password");
        String phone = RequestParamUtil.getString(req, "phone");

        if (name == null || email == null || password == null) {
            req.setAttribute("error", "Name, email and password are required.");
            forwardLogin(req, resp);
            return;
        }

        try {
            User existing = userDAO.findByEmail(email);
            if (existing != null) {
                req.setAttribute("error", "Email already registered.");
                forwardLogin(req, resp);
                return;
            }

            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(PasswordUtil.hash(password));
            u.setPhone(phone);
            u.setRole("USER");
            u.setCreatedAt(LocalDateTime.now().format(dtf));

            int id = userDAO.create(u);
            u.setId(id);

            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            req.setAttribute("error", "Registration failed: " + e.getMessage());
            forwardLogin(req, resp);
        }
    }

    private void forwardLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/login.jsp");
        rd.forward(req, resp);
    }
}
