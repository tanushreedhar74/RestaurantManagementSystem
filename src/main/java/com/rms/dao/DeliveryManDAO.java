package com.rms.dao;

import com.rms.model.DeliveryMan;
import com.rms.util.DBConnection;
import com.rms.util.PasswordHash;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryManDAO {

    // 🔹 Admin adds delivery man
    public boolean addDeliveryMan(DeliveryMan dm) {
        String sql = "INSERT INTO delivery_man (name, email, password, phone, is_available) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dm.getName());
            ps.setString(2, dm.getEmail());
            ps.setString(3, dm.getPassword()); // hashed
            ps.setString(4, dm.getPhone());
            ps.setBoolean(5, true);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Delivery man login
    public DeliveryMan login(String email, String password) {
        String sql = "SELECT * FROM delivery_man WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, PasswordHash.hashPassword(password)); // hash here

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DeliveryMan dm = new DeliveryMan();
                dm.setDeliveryManId(rs.getInt("delivery_man_id"));
                dm.setName(rs.getString("name"));
                dm.setEmail(rs.getString("email"));
                dm.setPhone(rs.getString("phone"));
                dm.setAvailable(rs.getBoolean("is_available"));
                return dm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Get available delivery men (for admin assignment)
    public List<DeliveryMan> getAvailableDeliveryMen() {
        List<DeliveryMan> list = new ArrayList<>();
        String sql = "SELECT * FROM delivery_man WHERE is_available = TRUE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryMan dm = new DeliveryMan();
                dm.setDeliveryManId(rs.getInt("delivery_man_id"));
                dm.setName(rs.getString("name"));
                dm.setEmail(rs.getString("email"));
                dm.setPhone(rs.getString("phone"));
                dm.setAvailable(true);
                list.add(dm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Update availability (core business rule)
    public boolean updateAvailability(int deliveryManId, boolean available) {
        String sql = "UPDATE delivery_man SET is_available = ? WHERE delivery_man_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, deliveryManId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<DeliveryMan> getAllDeliveryMen() {
        List<DeliveryMan> list = new ArrayList<>();

        String sql = "SELECT * FROM delivery_man";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryMan dm = new DeliveryMan();
                dm.setDeliveryManId(rs.getInt("delivery_man_id"));
                dm.setName(rs.getString("name"));
                dm.setEmail(rs.getString("email"));
                dm.setPhone(rs.getString("phone"));
                dm.setAvailable(rs.getBoolean("is_available"));
                list.add(dm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}