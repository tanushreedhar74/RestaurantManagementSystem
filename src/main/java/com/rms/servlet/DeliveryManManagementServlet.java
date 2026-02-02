package com.rms.servlet;

import com.rms.dao.DeliveryManDAO;
import com.rms.model.Admin;
import com.rms.model.DeliveryMan;
import com.rms.util.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class DeliveryManManagementServlet extends HttpServlet {

    private DeliveryManDAO deliveryManDAO;

    @Override
    public void init() {
        deliveryManDAO = new DeliveryManDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Admin admin = (session != null) ? (Admin) session.getAttribute("admin") : null;

        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp");
            return;
        }

        // ✅ SHOW ALL DELIVERY MEN
        List<DeliveryMan> deliveryMen = deliveryManDAO.getAllDeliveryMen();
        req.setAttribute("deliveryMen", deliveryMen);

        req.getRequestDispatcher("/admin/manage-delivery.jsp").forward(req, resp);
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

        if ("add".equalsIgnoreCase(action)) {
            addDeliveryMan(req, resp);
        } else if ("toggle".equalsIgnoreCase(action)) {
            toggleAvailability(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    // ================= ADD DELIVERY MAN =================
    private void addDeliveryMan(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = PasswordHash.hashPassword(req.getParameter("password"));
        String phone = req.getParameter("phone");

        DeliveryMan dm = new DeliveryMan(name, email, password, phone);
        deliveryManDAO.addDeliveryMan(dm);

        resp.sendRedirect(req.getContextPath() + "/admin/manage-delivery");
    }

    // ================= TOGGLE AVAILABILITY =================
    private void toggleAvailability(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int deliveryManId = Integer.parseInt(req.getParameter("deliveryManId"));
        boolean availability = Boolean.parseBoolean(req.getParameter("availability"));

        deliveryManDAO.updateAvailability(deliveryManId, availability);

        resp.sendRedirect(req.getContextPath() + "/admin/manage-delivery");

    }
}