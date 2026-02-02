package com.rms.servlet;

import com.rms.dao.OrderDAO;
import com.rms.dao.PaymentDAO;
import com.rms.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        paymentDAO = new PaymentDAO();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("onlineSuccess".equalsIgnoreCase(action)) {
            handleOnlinePaymentSuccess(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment action");
        }
    }

    // ================= ONLINE PAYMENT SUCCESS =================
    private void handleOnlinePaymentSuccess(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/login.jsp");
            return;
        }

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        // 1️⃣ Mark payment as PAID
        paymentDAO.markPaymentPaid(orderId);

        // 2️⃣ Update order payment status
        orderDAO.updatePaymentStatus(orderId, "PAID");

        // 3️⃣ Redirect to orders page
        resp.sendRedirect(req.getContextPath() + "/customer/orders.jsp");
    }
}