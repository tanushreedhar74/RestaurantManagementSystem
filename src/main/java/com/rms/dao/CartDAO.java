package com.rms.dao;

import com.rms.model.Cart;
import com.rms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // 🔹 Add item to cart
    public boolean addToCart(Cart cart) {
        String checkSql = "SELECT quantity FROM cart WHERE customer_id = ? AND dish_id = ?";
        String insertSql = "INSERT INTO cart (customer_id, dish_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE customer_id = ? AND dish_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            // Check if dish already exists in cart
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setInt(1, cart.getCustomerId());
                checkPs.setInt(2, cart.getDishId());
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    // Update quantity
                    try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                        updatePs.setInt(1, cart.getQuantity());
                        updatePs.setInt(2, cart.getCustomerId());
                        updatePs.setInt(3, cart.getDishId());
                        return updatePs.executeUpdate() > 0;
                    }
                } else {
                    // Insert new item
                    try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                        insertPs.setInt(1, cart.getCustomerId());
                        insertPs.setInt(2, cart.getDishId());
                        insertPs.setInt(3, cart.getQuantity());
                        return insertPs.executeUpdate() > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Get cart items of a customer
    public List<Cart> getCartByCustomer(int customerId) {
        List<Cart> list = new ArrayList<>();

        String sql = """
            SELECT c.cart_id, c.dish_id, d.dish_name, d.price, c.quantity
            FROM cart c
            JOIN dishes d ON c.dish_id = d.dish_id
            WHERE c.customer_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cart c = new Cart();
                c.setCartId(rs.getInt("cart_id"));
                c.setDishId(rs.getInt("dish_id"));
                c.setDishName(rs.getString("dish_name"));
                c.setPrice(rs.getBigDecimal("price"));
                c.setQuantity(rs.getInt("quantity"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Remove single item from cart
    public boolean removeFromCart(int cartId) {
        String sql = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Clear cart after order placed
    public boolean clearCart(int customerId) {
        String sql = "DELETE FROM cart WHERE customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
