package com.rms.servlet;

import com.rms.dao.DeliveryAssignmentDAO;
import com.rms.dao.DeliveryManDAO;
import com.rms.dao.OrderDAO;
import com.rms.dao.PaymentDAO;
import com.rms.model.DeliveryAssignment;
import com.rms.model.DeliveryMan;
import com.rms.model.OrderItem;
import com.rms.model.Payment;
import com.rms.model.Order;


import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryStatusServlet extends HttpServlet {

    private DeliveryAssignmentDAO assignmentDAO;
    private DeliveryManDAO deliveryManDAO;
    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() {
        assignmentDAO = new DeliveryAssignmentDAO();
        deliveryManDAO = new DeliveryManDAO();
        orderDAO = new OrderDAO();
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        DeliveryMan dm = (session != null)
                ? (DeliveryMan) session.getAttribute("deliveryMan")
                : null;

        if (dm == null) {
            resp.sendRedirect(req.getContextPath() + "/delivery/login.jsp");
            return;
        }

        DeliveryAssignment assignment =
                assignmentDAO.getAssignmentByDeliveryMan(dm.getDeliveryManId());

        if (assignment != null) {

            int orderId = assignment.getOrderId();

            Order order = orderDAO.getOrderById(orderId);
            List<OrderItem> items =
                    orderDAO.getItemsByOrderId(orderId);
            System.out.println("Order ID: " + orderId);
            System.out.println("Items size: " + items.size());
            Payment payment =
                    paymentDAO.getPaymentByOrderId(orderId);

            List<Order> orderList = new ArrayList<>();
            orderList.add(order);

            List<List<OrderItem>> itemsList = new ArrayList<>();
            itemsList.add(items);

            List<Payment> paymentList = new ArrayList<>();
            paymentList.add(payment);

            req.setAttribute("orderList", orderList);
            req.setAttribute("itemsList", itemsList);
            req.setAttribute("paymentList", paymentList);
        }

        req.getRequestDispatcher("/delivery/dashboard.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 🔐 Delivery man session check
        HttpSession session = req.getSession(false);
        DeliveryMan dm = (session != null) ? (DeliveryMan) session.getAttribute("deliveryMan") : null;

        if (dm == null) {
            resp.sendRedirect(req.getContextPath() + "/delivery/login.jsp");
            return;
        }

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String status = req.getParameter("status"); 
        if (status == null) {
            resp.sendRedirect(req.getContextPath() + "/delivery/status");
            return;
        }

        // PICKED_UP | OUT_FOR_DELIVERY | DELIVERED
        String paymentMode = req.getParameter("paymentMode"); 
        // COD | ONLINE (sent from JSP for final step)

        // 1️⃣ Update delivery assignment status
        assignmentDAO.updateStatus(orderId, status);

        // 2️⃣ Sync order status
        if ("PICKED_UP".equalsIgnoreCase(status)) {
            orderDAO.updateOrderStatus(orderId, "PREPARING");
        } 
        else if ("OUT_FOR_DELIVERY".equalsIgnoreCase(status)) {
            orderDAO.updateOrderStatus(orderId, "OUT_FOR_DELIVERY");
        } 
        else if ("DELIVERED".equalsIgnoreCase(status)) {

            // Final delivery
            orderDAO.updateOrderStatus(orderId, "DELIVERED");

            // 3️⃣ If COD, mark payment as PAID
            if ("COD".equalsIgnoreCase(paymentMode)) {
                paymentDAO.markPaymentPaid(orderId);
                orderDAO.updatePaymentStatus(orderId, "PAID");
            }

            // 4️⃣ Make delivery man available again
            deliveryManDAO.updateAvailability(dm.getDeliveryManId(), true);
        }

        resp.sendRedirect(req.getContextPath() + "/delivery/status");
    }
}