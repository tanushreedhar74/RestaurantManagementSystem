package com.rms.servlet;

import com.rms.dao.CustomerDAO;
import com.rms.model.Customer;
import com.rms.util.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class CustomerAuthServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("register".equalsIgnoreCase(action)) {
            handleRegister(req, resp);
        } 
        else if ("login".equalsIgnoreCase(action)) {
            handleLogin(req, resp);
        } 
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    // ================= REGISTER =================
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        if (customerDAO.emailExists(email)) {
            req.setAttribute("error", "Email already registered");
            req.getRequestDispatcher("/customer/register.jsp").forward(req, resp);
            return;
        }

        // 🔐 Hash password
        String hashedPassword = PasswordHash.hashPassword(password);

        Customer customer = new Customer(name, email, hashedPassword, phone, address);

        boolean success = customerDAO.registerCustomer(customer);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/customer/login.jsp");
        } else {
            req.setAttribute("error", "Registration failed");
            req.getRequestDispatcher("/customer/register.jsp").forward(req, resp);
        }
    }

    // ================= LOGIN =================
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 🔐 Hash password before checking
        String hashedPassword = PasswordHash.hashPassword(password);

        Customer customer = customerDAO.loginCustomer(email, hashedPassword);

        if (customer != null) {
            HttpSession session = req.getSession();
            session.setAttribute("customer", customer);

            resp.sendRedirect(req.getContextPath() + "/customer/menu");

        } else {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/customer/login.jsp").forward(req, resp);
        }
    }
}