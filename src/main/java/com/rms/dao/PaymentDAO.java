package com.rms.dao;

import com.rms.model.Payment;
import com.rms.util.DBConnection;

import java.sql.*;

public class PaymentDAO {

    // 🔹 Create payment entry when order is placed
    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, payment_mode, payment_status) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMode());
            ps.setString(3, payment.getPaymentStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Mark payment as PAID (after online success or COD delivery)
    public boolean markPaymentPaid(int orderId) {
        String sql = """
            UPDATE payments
            SET payment_status = 'PAID',
                paid_at = CURRENT_TIMESTAMP
            WHERE order_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Get payment by order ID
    public Payment getPaymentByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setOrderId(rs.getInt("order_id"));
                p.setPaymentMode(rs.getString("payment_mode"));
                p.setPaymentStatus(rs.getString("payment_status"));
                p.setPaidAt(rs.getTimestamp("paid_at"));
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}