package com.rms.dao;

import com.rms.model.Dish;
import com.rms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {

    // 🔹 Add new dish (Admin)
    public boolean addDish(Dish dish) {
        String sql = "INSERT INTO dishes (dish_name, cuisine, price, availability) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dish.getDishName());
            ps.setString(2, dish.getCuisine());
            ps.setBigDecimal(3, dish.getPrice());
            ps.setBoolean(4, dish.isAvailability());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Get all available dishes (Customer)
    public List<Dish> getAvailableDishes() {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dishes WHERE availability = TRUE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dish d = new Dish();
                d.setDishId(rs.getInt("dish_id"));
                d.setDishName(rs.getString("dish_name"));
                d.setCuisine(rs.getString("cuisine"));
                d.setPrice(rs.getBigDecimal("price"));
                d.setAvailability(rs.getBoolean("availability"));
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Get all dishes (Admin)
    public List<Dish> getAllDishes() {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dishes";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dish d = new Dish();
                d.setDishId(rs.getInt("dish_id"));
                d.setDishName(rs.getString("dish_name"));
                d.setCuisine(rs.getString("cuisine"));
                d.setPrice(rs.getBigDecimal("price"));
                d.setAvailability(rs.getBoolean("availability"));
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Update dish availability (Admin)
    public boolean updateAvailability(int dishId, boolean availability) {
        String sql = "UPDATE dishes SET availability = ? WHERE dish_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, availability);
            ps.setInt(2, dishId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Delete dish (Admin)
    public boolean deleteDish(int dishId) {
        String sql = "DELETE FROM dishes WHERE dish_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
