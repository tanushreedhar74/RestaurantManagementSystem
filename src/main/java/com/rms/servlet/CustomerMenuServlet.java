package com.rms.servlet;

import com.rms.dao.DishDAO;
import com.rms.model.Dish;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CustomerMenuServlet extends HttpServlet {

    private DishDAO dishDAO;

    @Override
    public void init() throws ServletException {
        dishDAO = new DishDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Fetch available dishes
        List<Dish> list = dishDAO.getAvailableDishes();

        // Store in request scope
        req.setAttribute("dishes", list);

        // Forward to JSP
        req.getRequestDispatcher("/customer/menu.jsp").forward(req, resp);

    }
}