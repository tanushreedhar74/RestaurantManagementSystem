package com.rms.servlet;

import com.rms.dao.CartDAO;
import com.rms.model.Cart;
import com.rms.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CartServlet extends HttpServlet {

    private CartDAO cartDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            addToCart(req, resp);
        } else if ("remove".equalsIgnoreCase(action)) {
            removeFromCart(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        viewCart(req, resp);
    }

    // ================= ADD TO CART =================
    private void addToCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/login.jsp");
            return;
        }

        int dishId = Integer.parseInt(req.getParameter("dishId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        Cart cart = new Cart(customer.getCustomerId(), dishId, quantity);
        cartDAO.addToCart(cart);

        resp.sendRedirect(req.getContextPath() + "/customer/menu");


    }

    // ================= REMOVE FROM CART =================
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int cartId = Integer.parseInt(req.getParameter("cartId"));
        cartDAO.removeFromCart(cartId);

        resp.sendRedirect(req.getContextPath() + "/customer/cart");

    }

 // ================= VIEW CART =================
    private void viewCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        int customerId = customer.getCustomerId();

        // fetch cart items
        List<Cart> cartList = cartDAO.getCartByCustomer(customerId);

        // calculate total
        double totalAmount = 0;
        for (Cart c : cartList) {
        	totalAmount += c.getPrice().doubleValue() * c.getQuantity();
        }

        // set attributes
        req.setAttribute("cartList", cartList);
        req.setAttribute("totalAmount", totalAmount);

        // forward to JSP
        req.getRequestDispatcher("/customer/cart.jsp").forward(req, resp);
    }
}