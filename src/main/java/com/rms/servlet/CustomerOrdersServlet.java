package com.rms.servlet;

import com.rms.dao.*;
import com.rms.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class CustomerOrdersServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/login.jsp");
            return;
        }

        int customerId = customer.getCustomerId();

        // 1️⃣ Fetch orders
        List<Order> orders = orderDAO.getOrdersByCustomer(customerId);

        // 2️⃣ Fetch payments for each order
        List<Payment> paymentList = new ArrayList<>();
        List<List<OrderItem>> allItems = new ArrayList<>();

        for (Order o : orders) {
            Payment p = paymentDAO.getPaymentByOrderId(o.getOrderId());
            paymentList.add(p);

            List<OrderItem> items = orderItemDAO.getItemsByOrderId(o.getOrderId());
            allItems.add(items);
        }

        req.setAttribute("orderList", orders);
        req.setAttribute("paymentList", paymentList);
        req.setAttribute("itemsList", allItems);

        req.getRequestDispatcher("/customer/orders.jsp").forward(req, resp);
    }
}