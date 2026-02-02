package com.rms.dao;

import com.rms.model.DeliveryAssignment;
import com.rms.util.DBConnection;

import java.sql.*;

public class DeliveryAssignmentDAO {

    // 🔹 Assign delivery man to order (Admin)
    public boolean assignDeliveryMan(DeliveryAssignment da) {
        String sql = "INSERT INTO delivery_assignment (order_id, delivery_man_id, status) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, da.getOrderId());
            ps.setInt(2, da.getDeliveryManId());
            ps.setString(3, da.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Update delivery status (Delivery Man)
    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE delivery_assignment SET status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Get assignment by delivery man
    public DeliveryAssignment getAssignmentByDeliveryMan(int deliveryManId) {
        String sql = "SELECT * FROM delivery_assignment WHERE delivery_man_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deliveryManId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DeliveryAssignment da = new DeliveryAssignment();
                da.setAssignmentId(rs.getInt("assignment_id"));
                da.setOrderId(rs.getInt("order_id"));
                da.setDeliveryManId(rs.getInt("delivery_man_id"));
                da.setStatus(rs.getString("status"));
                return da;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Check if delivery man already has active assignment
    public boolean hasActiveAssignment(int deliveryManId) {
        String sql = """
            SELECT assignment_id
            FROM delivery_assignment
            WHERE delivery_man_id = ?
              AND status != 'DELIVERED'
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deliveryManId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isOrderAssigned(int orderId) {
        String sql = "SELECT 1 FROM delivery_assignment WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
