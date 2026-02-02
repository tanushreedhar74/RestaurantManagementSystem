package com.rms.servlet;

import com.rms.dao.AdminDAO;
import com.rms.model.Admin;
import com.rms.util.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminLoginServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 🔐 Hash password before checking
        String hashedPassword = PasswordHash.hashPassword(password);

        Admin admin = adminDAO.loginAdmin(email, hashedPassword);

        if (admin != null) {
            HttpSession session = req.getSession();
            session.setAttribute("admin", admin);

            resp.sendRedirect(req.getContextPath() + "/admin/dashboard.jsp");
        } else {
            req.setAttribute("error", "Invalid admin credentials");
            req.getRequestDispatcher("/admin/login.jsp").forward(req, resp);
        }
    }
}