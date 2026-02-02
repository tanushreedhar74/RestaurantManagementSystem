package com.rms.servlet;

import com.rms.dao.*;
import com.rms.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private CartDAO cartDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        cartDAO = new CartDAO();
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("place".equalsIgnoreCase(action)) {
            placeOrder(req, resp);
        } else if ("cancel".equalsIgnoreCase(action)) {
            cancelOrder(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    // ================= PLACE ORDER =================
    private void placeOrder(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/login.jsp");
            return;
        }

        String paymentMode = req.getParameter("paymentMode"); // COD / ONLINE

        // 1️⃣ Get cart items
        List<Cart> cartItems = cartDAO.getCartByCustomer(customer.getCustomerId());

        if (cartItems.isEmpty()) {
            req.setAttribute("error", "Cart is empty");
            req.getRequestDispatcher("/customer/cart.jsp").forward(req, resp);
            return;
        }

        // 2️⃣ Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart c : cartItems) {
            BigDecimal itemTotal = c.getPrice().multiply(BigDecimal.valueOf(c.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 3️⃣ Create Order
        Order order = new Order(customer.getCustomerId(), totalAmount);
        int orderId = orderDAO.createOrder(order);

        if (orderId == -1) {
            req.setAttribute("error", "Order creation failed");
            req.getRequestDispatcher("/customer/cart.jsp").forward(req, resp);
            return;
        }

        // 4️⃣ Insert Order Items
        for (Cart c : cartItems) {
            OrderItem item = new OrderItem(
                    orderId,
                    c.getDishId(),
                    c.getQuantity(),
                    c.getPrice()
            );
            orderItemDAO.addOrderItem(item);
        }

        // 5️⃣ Create Payment Entry
        Payment payment = new Payment(orderId, paymentMode);
        paymentDAO.createPayment(payment);

        // 6️⃣ Clear Cart
        cartDAO.clearCart(customer.getCustomerId());

        // 7️⃣ Redirect to orders page
        resp.sendRedirect(req.getContextPath() + "/customer/orders");

    }

    // ================= CANCEL ORDER =================
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        boolean cancelled = orderDAO.cancelOrder(orderId);

        resp.sendRedirect(req.getContextPath() + "/customer/orders");

    }
}