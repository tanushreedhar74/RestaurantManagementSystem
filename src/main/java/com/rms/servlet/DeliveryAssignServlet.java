package com.rms.servlet;

import com.rms.dao.DeliveryAssignmentDAO;
import com.rms.dao.DeliveryManDAO;
import com.rms.dao.OrderDAO;
import com.rms.model.Admin;
import com.rms.model.DeliveryAssignment;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class DeliveryAssignServlet extends HttpServlet {

    private DeliveryAssignmentDAO deliveryAssignmentDAO;
    private DeliveryManDAO deliveryManDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        deliveryAssignmentDAO = new DeliveryAssignmentDAO();
        deliveryManDAO = new DeliveryManDAO();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 🔐 Admin session check
        HttpSession session = req.getSession(false);
        Admin admin = (session != null) ? (Admin) session.getAttribute("admin") : null;

        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp");
            return;
        }

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int deliveryManId = Integer.parseInt(req.getParameter("deliveryManId"));

        // 🚨 Rule: delivery man can have only one active order
        if (deliveryAssignmentDAO.hasActiveAssignment(deliveryManId)) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders.jsp?error=delivery_busy");
            return;
        }

        // 1️⃣ Assign delivery man
        DeliveryAssignment assignment =
                new DeliveryAssignment(orderId, deliveryManId);
        deliveryAssignmentDAO.assignDeliveryMan(assignment);

        // 2️⃣ Update delivery man availability
        deliveryManDAO.updateAvailability(deliveryManId, false);

        // 3️⃣ Update order status
        orderDAO.updateOrderStatus(orderId, "PREPARING");

        // 4️⃣ Redirect back to admin orders page
        resp.sendRedirect(req.getContextPath() + "/admin/orders.jsp?success=assigned");
    }
}