package com.rms.servlet;

import com.rms.dao.*;
import com.rms.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderApprovalServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private DeliveryManDAO deliveryManDAO;
    private DeliveryAssignmentDAO deliveryAssignmentDAO;
    private OrderItemDAO orderItemDAO;
    private PaymentDAO paymentDAO;


    @Override
    public void init() {
        orderDAO = new OrderDAO();
        deliveryManDAO = new DeliveryManDAO();
        deliveryAssignmentDAO = new DeliveryAssignmentDAO();
        orderItemDAO = new OrderItemDAO();   
        paymentDAO = new PaymentDAO();  
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 🔐 Admin session check
        HttpSession session = req.getSession(false);
        Admin admin = (session != null) ? (Admin) session.getAttribute("admin") : null;

        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp");
            return;
        }

        // Fetch orders & delivery men
        List<Order> orders = orderDAO.getAllOrders();
        List<DeliveryMan> availableDeliveryMen = deliveryManDAO.getAvailableDeliveryMen();

        // ===== REQUIRED FOR JSP =====
        List<List<OrderItem>> itemsList = new ArrayList<>();
        List<Payment> paymentList = new ArrayList<>();
        List<Integer> assignedList = new ArrayList<>();

        for (Order o : orders) {

            // order items
            itemsList.add(orderItemDAO.getItemsByOrderId(o.getOrderId()));

            // payment
            paymentList.add(paymentDAO.getPaymentByOrderId(o.getOrderId()));

            // assignment status
            if (deliveryAssignmentDAO.isOrderAssigned(o.getOrderId())) {
                assignedList.add(o.getOrderId());
            }
        }

        // send data to JSP
        req.setAttribute("orderList", orders);
        req.setAttribute("deliveryMen", availableDeliveryMen);
        req.setAttribute("itemsList", itemsList);
        req.setAttribute("paymentList", paymentList);
        req.setAttribute("assignedList", assignedList);

        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
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

        String action = req.getParameter("action");

        if ("approve".equalsIgnoreCase(action)) {
            approveOrder(req, resp);
        } else if ("assign".equalsIgnoreCase(action)) {
            assignDeliveryMan(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    // ================= APPROVE ORDER =================
    private void approveOrder(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        // Change order status to APPROVED
        orderDAO.updateOrderStatus(orderId, "APPROVED");

        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }

    // ================= ASSIGN DELIVERY MAN =================
    private void assignDeliveryMan(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int deliveryManId = Integer.parseInt(req.getParameter("deliveryManId"));

        // 🚨 Check if delivery man already has active assignment
        if (deliveryAssignmentDAO.hasActiveAssignment(deliveryManId)) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?error=busy");
            return;
        }

        // 1️⃣ Create assignment
        DeliveryAssignment da = new DeliveryAssignment(orderId, deliveryManId);
        deliveryAssignmentDAO.assignDeliveryMan(da);

        // 2️⃣ Update delivery man availability
        deliveryManDAO.updateAvailability(deliveryManId, false);

        // 3️⃣ Update order status
        orderDAO.updateOrderStatus(orderId, "PREPARING");

        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}