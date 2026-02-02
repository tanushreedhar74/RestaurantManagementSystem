package com.rms.servlet;

import com.rms.dao.DeliveryManDAO;
import com.rms.model.DeliveryMan;
import com.rms.util.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class DeliveryLoginServlet extends HttpServlet {

    private DeliveryManDAO deliveryManDAO;

    @Override
    public void init() {
        deliveryManDAO = new DeliveryManDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 🔐 Hash password before checking
        DeliveryMan deliveryMan = deliveryManDAO.login(email, password);

        if (deliveryMan != null) {
            HttpSession session = req.getSession();
            session.setAttribute("deliveryMan", deliveryMan);

            resp.sendRedirect(req.getContextPath() + "/delivery/status");

        } else {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/delivery/login.jsp").forward(req, resp);
        }
    }
}