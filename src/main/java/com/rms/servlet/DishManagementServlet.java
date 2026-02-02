package com.rms.servlet;

import com.rms.dao.DishDAO;
import com.rms.model.Admin;
import com.rms.model.Dish;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class DishManagementServlet extends HttpServlet {

    private DishDAO dishDAO;

    @Override
    public void init() {
        dishDAO = new DishDAO();
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
            addDish(req, resp);
        } else if ("toggle".equalsIgnoreCase(action)) {
            toggleAvailability(req, resp);
        } else if ("delete".equalsIgnoreCase(action)) {
            deleteDish(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
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

        // View all dishes
        List<Dish> dishes = dishDAO.getAllDishes();
        req.setAttribute("dishList", dishes);
        req.getRequestDispatcher("/admin/manage-dishes.jsp").forward(req, resp);
    }

    // ================= ADD DISH =================
    private void addDish(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String name = req.getParameter("dishName");
        if (name == null || name.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dishes");
            return;
        }
        String cuisine = req.getParameter("cuisine");
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        boolean availability = Boolean.parseBoolean(req.getParameter("availability"));

        Dish dish = new Dish(name, cuisine, price);
        dish.setAvailability(availability);
        dishDAO.addDish(dish);

        resp.sendRedirect(req.getContextPath() + "/admin/dishes");
    }

    // ================= TOGGLE AVAILABILITY =================
    private void toggleAvailability(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int dishId = Integer.parseInt(req.getParameter("dishId"));
        boolean availability = Boolean.parseBoolean(req.getParameter("availability"));

        dishDAO.updateAvailability(dishId, availability);

        resp.sendRedirect(req.getContextPath() + "/admin/dishes");
    }

    // ================= DELETE DISH =================
    private void deleteDish(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int dishId = Integer.parseInt(req.getParameter("dishId"));
        dishDAO.deleteDish(dishId);

        resp.sendRedirect(req.getContextPath() + "/admin/dishes");
    }
}